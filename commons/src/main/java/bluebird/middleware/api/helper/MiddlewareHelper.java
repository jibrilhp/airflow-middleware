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
package jibs.middleware.api.helper;

import jibs.middleware.db.MiddlewareDB;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import jibs.airflow.middlewaredb.tables.pojos.Middleware;
import jibs.middleware.api.datahub.fetch.DataJob;
import jibs.middleware.api.datahub.push.Aspect;
import jibs.middleware.api.datahub.push.Proposals;
import jibs.middleware.api.datahub.push.Proposal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.micronaut.context.annotation.Value;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jibrilhp
 */
public class MiddlewareHelper {

    @MiddlewareDB
    @Inject
    private Provider<DataSource> middlewareDatasourceProvider;

    @SuppressWarnings({"java:S6212", "java:S6213"})
    private Middleware newMiddlewareRecord(String tableName, String dagTargetName, String dagSourceName, OffsetDateTime nowTs) {
        //trigger_target_dag
        //table_name : data-prod-1902.mart_taxi_order.taxi_order

        Middleware record = new Middleware();
        record.setTriggerName(tableName);
        record.setTriggerTargetDag(dagTargetName);
        record.setTriggerSourceDag(dagSourceName);

        record.setCreatedAt(nowTs);

        record.setCreatedAt(nowTs);
        record.setUpdatedAt(nowTs);

        return record;
    }

    private static String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    private static Duration getDuration(String triggerIntervalAirflow) {
        int value = Integer.parseInt(triggerIntervalAirflow.substring(0, triggerIntervalAirflow.length() - 1));
        String unit = triggerIntervalAirflow.substring(triggerIntervalAirflow.length() - 1);

        Duration interval;
        switch (unit) {
            case "m":
                interval = Duration.ofMinutes(value);
                break;
            case "h":
                interval = Duration.ofHours(value);
                break;
            case "d":
                interval = Duration.ofDays(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid interval unit: " + unit);
        }

        System.out.println("INTERVAL: " + interval.toString());
        return interval;
    }

    public static Map<String, String> updateToDatahub(String hostGms, String hostFrontend, String token, String urn, String urnFull,  String triggerIntervalAirflow) {
        // Get current parameters from datahub

        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> responseDatahub = new HashMap<>();

        try {

            String encodedUrn = encodeValue(urn);
            System.out.println("Write this " + encodedUrn);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(hostGms + "/entitiesV2/" + encodedUrn))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
//            System.out.println("wait " + response.body());

            if (response == null) {
                responseDatahub.put("object_status", "not_found");
                responseDatahub.put("error", "GMS not active");
            }

            JSONObject jsonObject = new JSONObject(response.body());
            
           
            JSONObject aspects = jsonObject.getJSONObject("aspects");
            final JSONObject[] customProp = {null};

            if (!aspects.isNull("dataJobInfo")) {
                JSONObject dataJobInfo = aspects.getJSONObject("dataJobInfo");
                customProp[0] = dataJobInfo.getJSONObject("value").getJSONObject("customProperties");
            } else {
                JSONObject dataFlowInfo = aspects.getJSONObject("dataFlowInfo");
                customProp[0] = dataFlowInfo.getJSONObject("value").getJSONObject("customProperties");
            }

            Map<String, String> customPropertiesMap = new HashMap<>();

            OffsetDateTime offsetDateTime = OffsetDateTime.now(ZoneId.of("Asia/Jakarta"));
//            customPropertiesMap.put("evd_last_ingestion", offsetDateTime.toString());

            if (customProp[0] != null) {
                JSONObject customProperties = customProp[0];
                customProperties.keys().forEachRemaining(key -> {
//                System.out.println(key);
                    if (!key.contains("openlineage_") && !key.contains("doc_md")) {
                        if (key.equals("evd_last_ingestion")) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                              
                            String tmpLastIngestion;
                            try {
                                tmpLastIngestion = customProperties.getString("evd_last_ingestion_temp");
                            } catch (Exception x) {
                                tmpLastIngestion = customProperties.getString("evd_last_ingestion");
                            }
                            responseDatahub.put("evd_last_ingestion_temp",tmpLastIngestion );
                            responseDatahub.put("evd_last_ingestion", offsetDateTime.format(formatter));

                            customPropertiesMap.put("evd_last_ingestion_temp", tmpLastIngestion);
                            customPropertiesMap.put("evd_last_ingestion", offsetDateTime.format(formatter));

                        } else {
                            String value = customProperties.getString(key);
                            customPropertiesMap.put(key, value);
                        }
                    }

                });

                if (responseDatahub.containsKey("evd_last_ingestion")) {
                    try {
                        
                        System.out.println("GET " + responseDatahub.get("evd_last_ingestion_temp"));
                        System.out.println("POST " + responseDatahub.get("evd_last_ingestion"));   
                        
                        LocalDateTime dateTimeStart = LocalDateTime.parse(responseDatahub.get("evd_last_ingestion_temp"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        LocalDateTime dateTimeLastStart = LocalDateTime.parse(responseDatahub.get("evd_last_ingestion"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        // Calculate the difference between the two date-times
                        Duration duration = Duration.between(dateTimeStart, dateTimeLastStart);
//                     System.out.println(duration.compareTo(getDuration(triggerIntervalAirflow)));
                        if (!(duration.compareTo(getDuration(triggerIntervalAirflow)) >= 0)) {
                            // Not met requirements for 5 minutes or more ~
                            responseDatahub.put("object_status", "wait_for_schedule");
                            responseDatahub.put("error", "Not met");
                            return responseDatahub;
                        }

                    } catch (DateTimeParseException ex) {
                        System.out.println(ex.getMessage());
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                ObjectMapper objectMapper = new ObjectMapper();
                customPropertiesMap.put("evd_last_ingestion", offsetDateTime.toString());
                responseDatahub.put("properties_value", objectMapper.writeValueAsString(customPropertiesMap));

                String patternTask = "urn:li:dataJob:\\(urn:li:dataFlow:\\(airflow,(\\w+),prod\\),(\\w+)\\)";
                Pattern pattern = Pattern.compile(patternTask);
                Matcher matcher = pattern.matcher(urnFull);

                Gson gson = new GsonBuilder().disableHtmlEscaping()
                        .create();
                Map<String, Object> h = new HashMap<>();
                Map<String, String> typ = new HashMap<>();
                typ.put("string", "COMMAND");
                h.put("customProperties", customPropertiesMap);
                if (matcher.find()) {
                    String taskName = matcher.group(2);
                    h.put("name", taskName);
                } else {
                    h.put("name", "params_eval");
                }

                h.put("type", typ);

//                Proposals proposal = new Proposals();
//                Proposal proposalContent = new Proposal();
//                Aspect aspect = new Aspect();
//                aspect.setValue(gson.toJson(h));
//
//                proposalContent.setEntityType("dataJob");
//                proposalContent.setEntityUrn(urn);
//                proposalContent.setChangeType("UPSERT");
//                proposalContent.setAspectName("dataJobInfo");
//                proposalContent.setAspect(aspect);
//                aspect.setContentType("application/json");
//
//                proposal.setProposal(proposalContent);
//                String jsonString = gson.toJson(proposal).replace("\\\\\\", "\\");

//            System.out.println(jsonString);
//                request = HttpRequest.newBuilder()
//                        .uri(URI.create(hostGms + "/aspects?action=ingestProposal"))
//                        .header("Content-Type", "application/json")
//                        .header("Authorization", String.format("Bearer %s", token))
//                        .POST(HttpRequest.BodyPublishers.ofString(jsonString))
//                        .build();
//
//                HttpResponse<String> responses = client.send(request, HttpResponse.BodyHandlers.ofString());

//            System.out.println(responses.body());
//                DataJob dataJob = gson.fromJson(responses.body(), DataJob.class);
                
//                if (!dataJob.getValue().isEmpty()) {
//                    responseDatahub.put("object_status", dataJob.getValue());
//                } else {
//                    responseDatahub.put("object_status", "not_found");
//                }
            }
//            System.out.println(responseDatahub);

        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        responseDatahub.put("object_status", "success");

        responseDatahub.put("error", "None");

        return responseDatahub;

    }

//     @SuppressWarnings({"java:S6212", "java:S6213"})
//    public void checkThreshold(String tableName) throws Exception {
//        MtServerConfigDao registryDao = registryProvider.get();
//        FdwDeployDao fdwDao = fdwDaoInitializer.get();
//
//        MtServerConfigRecord serverCfg = registryDao.getByPool(kodePool);
//        if (serverCfg == null) {
//            L.info("Pool '{}' not found at registry, mark as delete", kodePool);
//            fdwDao.updateDBStatusByID(kodePool, DbStatus.DELETED);
//            return;
//        }
//
//        FdwDeploy record = newFdwRecord(serverCfg, OffsetDateTime.now());
//        Arrays.asList(record);
//        int sz = fdwDao.upsertServerInfo(Arrays.asList(record));
//        L.info("{} DB Server added/updated", sz);
//
//      
//    }
}
