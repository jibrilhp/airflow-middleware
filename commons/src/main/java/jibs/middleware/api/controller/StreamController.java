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

/**
 *
 * @author jibrilhp
 */


import jibs.middleware.api.airflow.TriggerDAG;
import jibs.middleware.api.datahub.SearchUrn;
import jibs.middleware.api.datahub.fetch.RootObject;
import jibs.middleware.api.datahub.fetch.SearchResult;
import jibs.middleware.api.datahub.fetch.SearchResults;
import jibs.middleware.api.parameters.datahub.DatahubParameters;
import jibs.middleware.api.parameters.stream.StreamParameters;
import jibs.middleware.api.parameters.stream.response.DataResult;
import jibs.middleware.api.parameters.stream.response.ReturnStreamController;
import jibs.middleware.config.DatahubConfig;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.databind.ObjectWriter; 
import jakarta.inject.Singleton;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller("/api/stream/push") 
@Singleton
public class StreamController {
    
   private final String token;
   private final String datahubHostFrontend;   
   private final String datahubHostGms;
   private final String airflowHost;
   private final String airflowToken;
   private final String airflowTriggerInterval;
   
   private static final Logger L = LoggerFactory.getLogger(StreamController.class);
         
  public StreamController(@Value("${datahub.token}") String token, @Value("${datahub.host-frontend}") String hostFrontend, @Value("${datahub.host-gms}") String hostGms,@Value("${airflow.trigger-interval}") String airflowTriggerInterval, @Value("${airflow.host}") String airflowHost, @Value("${airflow.authorization}") String airflowToken) {
    this.token = token;
    this.datahubHostFrontend = hostFrontend;
    this.airflowHost = airflowHost;
    this.airflowToken = airflowToken;
    this.datahubHostGms = hostGms;
    this.airflowTriggerInterval = airflowTriggerInterval;
  }
  
    
    
  @Post(produces = MediaType.APPLICATION_JSON) 
    public HttpResponse<?> push(@Body StreamParameters data) throws IOException, InterruptedException{
        // received dataset and table, scan on datahub , return dag name and trigger the dag 
        
        
          
        ObjectWriter owp = new ObjectMapper().writer().withDefaultPrettyPrinter();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        SearchUrn su = new SearchUrn();
        RootObject rootObject = objectMapper.readValue(su.Search(data.getProject_id(),data.getTarget_dataset(),data.getTarget_table(),datahubHostFrontend,token), RootObject.class);
        DataResult dataResponse = new DataResult();
        ReturnStreamController rso = new ReturnStreamController();
        
        
        if (rootObject.getData().getSearchAcrossLineage().getSearchResults().length > 0) { 
            for (SearchResult searchResult : rootObject.getData().getSearchAcrossLineage().getSearchResults()) {
                
                String regexPattern = "(?<=airflow,)(.*?)(?=,prod)";
                
                Pattern pattern = Pattern.compile(regexPattern);
                Matcher matcherAirflowDAG = pattern.matcher(searchResult.getEntity().getUrn());

                if (matcherAirflowDAG.find()) {
//                    System.out.println("OK: " + searchResult.getEntity().getUrn());
                    String triggerDAG = matcherAirflowDAG.group(1);
                    // check threshold first & datahub
                    L.info("Triggering : " + triggerDAG);
                    
                    String airflowUrn = String.format("urn:li:dataFlow:(airflow,%s,prod)",triggerDAG);
                    // urn:li:dataFlow:(airflow,datamart_pool_monthly,prod)
                    System.out.println(airflowUrn);
       
                    String resultTrigger = TriggerDAG.Trigger( airflowUrn, searchResult.getEntity().getUrn(), triggerDAG,airflowHost,airflowToken,datahubHostGms,datahubHostFrontend, token,airflowTriggerInterval);
                    
                    L.info(resultTrigger);
//                    System.out.println(resultTrigger);
                    dataResponse.setThreshold(resultTrigger);
                    dataResponse.setLastTriggered(OffsetDateTime.now().format(DateTimeFormatter.ISO_DATE));

                    dataResponse.setThreshold("true");

                    rso.setStatus(200);
                    rso.setData(dataResponse);
                } else {
                      L.info(String.format("No Airflow Lineage match found. Project %s Dataset: %s, Table %s URN %s ", data.getProject_id(),data.getTarget_dataset(),data.getTarget_table(),searchResult.getEntity().getUrn()));
//                      System.out.println(String.format("No match found. Project %s Dataset: %s, Table %s ", data.getProject_id(),data.getTarget_dataset(),data.getTarget_table()));
//                      System.out.println(searchResult.getEntity().getUrn());
                }
               
            }
           
        } else {
            
            rso.setStatus(404);
            rso.setData(null);
            
          
            String jsonReturn = owp.writeValueAsString(rso);
        
            return HttpResponse.notFound(jsonReturn);
        }
         
      
        String jsonReturn = owp.writeValueAsString(rso);

        return HttpResponse.ok(jsonReturn);
        }
    
    
}
