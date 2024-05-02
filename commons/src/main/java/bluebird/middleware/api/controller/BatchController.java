/*
 * Copyright 2024 jibs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jibs.middleware.api.controller;

import jibs.middleware.api.airflow.TriggerDAG;
import jibs.middleware.api.datahub.SearchUrn;
import jibs.middleware.api.datahub.batch.GraphQLResponse;
import jibs.middleware.api.datahub.batch.Tag;
import jibs.middleware.api.datahub.batch.TagWrapper;
import jibs.middleware.api.datahub.fetch.RootObject;
import jibs.middleware.api.datahub.fetch.SearchResult;
import jibs.middleware.api.parameters.batch.BatchParameters;
import jibs.middleware.api.helper.BatchHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jibrilhp
 */
@Controller("/api/batch/push")
@Singleton
public class BatchController {

    private final String token;
    private final String datahubHostFrontend;
    private final String datahubHostGms;
    private final String airflowHost;
    private final String airflowToken;
    private final String airflowTriggerInterval;

    public BatchController(@Value("${datahub.token}") String token, @Value("${datahub.host-frontend}") String datahubHostFrontend, @Value("${datahub.host-gms}") String datahubHostGms, @Value("${airflow.trigger-interval}") String airflowTriggerInterval, @Value("${airflow.host}") String airflowHost, @Value("${airflow.authorization}") String airflowToken) {
        this.token = token;
        this.datahubHostFrontend = datahubHostFrontend;
        this.datahubHostGms = datahubHostGms;
        this.airflowHost = airflowHost;
        this.airflowToken = airflowToken;
        this.airflowTriggerInterval = airflowTriggerInterval;
    }

    private static final Logger L = LoggerFactory.getLogger(BatchController.class);

    @Post(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> push(@Body BatchParameters data) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        BatchHelper bhr = new BatchHelper();

        String baseUrnTable = "urn:li:dataset:(urn:li:dataPlatform:bigquery,%s,PROD)";
        String urnTable = String.format(baseUrnTable, data.getProject_id() + "." + data.getTarget_dataset() + "." + data.getTarget_table());

        // check the downstream airflow 
        SearchUrn su = new SearchUrn();
        RootObject rootObject = objectMapper.readValue(su.Search(data.getProject_id(), data.getTarget_dataset(), data.getTarget_table(), datahubHostFrontend, token), RootObject.class);

        if (rootObject.getData().getSearchAcrossLineage().getSearchResults().length > 0) {
             
//            System.out.println(urnTable);
           
            for (SearchResult searchResult : rootObject.getData().getSearchAcrossLineage().getSearchResults()) {
                
                String regexPattern = "(?<=airflow,)(.*?)(?=,prod)";

                Pattern pattern = Pattern.compile(regexPattern);
                Matcher matcherAirflowDAG = pattern.matcher(searchResult.getEntity().getUrn());
                
                System.out.println(searchResult.getEntity().getUrn());

                if (matcherAirflowDAG.find()) {
                   

                    String triggerDAG = matcherAirflowDAG.group(1);
                    // check threshold first & datahub
                    L.info("Check Threshold : " + triggerDAG);

                    GraphQLResponse graphQLResponse = objectMapper.readValue(bhr.checkThreshold(String.format("urn:li:dataFlow:(airflow,%s,prod)", triggerDAG), datahubHostFrontend, datahubHostGms, token), GraphQLResponse.class);

                    try {
                        List<TagWrapper> tagWrappers = graphQLResponse.getData().getDataset().getTags().getTags();
                        for (TagWrapper tagWrapper : tagWrappers) {
                            Tag tag = tagWrapper.getTag();
                            L.info("Check Tag ...");
                            // filter only daily or hourly

                            if (tag.getUrn().contentEquals("urn:li:tag:daily")) {
//                                Map<String,String> resultDatahub = bhr.checkProperties(String.format("urn:li:dataFlow:(airflow,%s,prod)", triggerDAG), datahubHostFrontend, datahubHostGms, token, "1d");
                                Map<String,String> resultDatahub  = bhr.checkThresholdDataFlow(String.format("urn:li:dataFlow:(airflow,%s,prod)", triggerDAG), datahubHostFrontend, datahubHostGms, token);
                                Date currentDate = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                Date current_evd_last_ingestion = dateFormat.parse(resultDatahub.get("evd_last_ingestion"));
//                                Date current_now = currentDate;
                                Date current_now = dateFormat.parse("2024-05-30 00:00:00"); //TEST
                                
                                long timeDiffMillis = current_now.getTime() - current_evd_last_ingestion.getTime();
                                long hoursDiff = timeDiffMillis / (1000 * 60 * 60);
                                
                                System.out.println(hoursDiff);
                                
                                if (hoursDiff >= 24) {
                                    Map<String,String> resultDatahubT = new HashMap<>();
                                    current_evd_last_ingestion.setHours(0);
                                    current_evd_last_ingestion.setMinutes(0);
                                    current_evd_last_ingestion.setSeconds(0);
                                    
                                    resultDatahubT.put("evd_last_ingestion_temp", dateFormat.format(current_evd_last_ingestion));
                                    resultDatahubT.put("evd_last_ingestion", dateFormat.format(current_now));
                                    
                                    String resultTrigger = TriggerDAG.TriggerDaily(searchResult.getEntity().getUrn(), triggerDAG,airflowHost,airflowToken,datahubHostGms,datahubHostFrontend, token,resultDatahubT);
                                    L.info("Triggering ... " + resultTrigger);
                                } else {
                                    L.info("Skipped Trigger ... with difference " + String.valueOf(hoursDiff));
//                                    System.out.println(timeDiffMillis);
                                }
                                
                                
//                                resultDatahub.keySet().forEach(key -> {
//                                    System.out.println(key + "=>" + resultDatahub.get(key));
//                                    
//                                    
//                                });
//                                System.out.println("hello daily");
//                              
                                // check properties
                                
                                // check threshold
                                // decide the threshold
                                
                                
                            } else if (tag.getUrn().contentEquals("urn:li:tag:hourly")) {
                                L.info("Triggering Hourly isnt coded yet ... ");
                                System.out.println("hello hourly");
                            }
                        }
                    } catch (Exception ex) {
                        L.error( ex.getMessage());
                       
                        
                    }

                  

                } else {
//                      System.out.println(String.format("No Airflow Lineage match found. Project %s Dataset: %s, Table %s URN %s ", data.getProject_id(), data.getTarget_dataset(), data.getTarget_table(), searchResult.getEntity().getUrn()));
                    L.info(String.format("No Airflow Lineage match found. Project %s Dataset: %s, Table %s URN %s ", data.getProject_id(), data.getTarget_dataset(), data.getTarget_table(), searchResult.getEntity().getUrn()));
                }

            }

        }

        return HttpResponse.ok("ezez ");
    }
}
