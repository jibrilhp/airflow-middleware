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

import jibs.middleware.api.datahub.SearchUrn;
import jibs.middleware.api.datahub.flow.Aspect;
import jibs.middleware.api.datahub.flow.Proposal;
import jibs.middleware.api.datahub.flow.ProposalWrapper;
import jibs.middleware.api.parameters.datahub.DatahubParameters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;

/**
 *
 * @author jibrilhp
 */
public class BatchHelper {

    public String checkThreshold(String urn, String datahubHostFrontend, String datahubHostGMS, String token) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> responseDatahub = new HashMap<>();

        String query = String.format("""
                query {
                  dataset(urn: "%s") {
                    tags {
                      tags {
                        tag {
                          name
                          urn
                          
                        }
                      }
                    }
                  }
                }
                """, urn);

//        System.out.println(query);
        DatahubParameters paramPost = new DatahubParameters();
        paramPost.setOperationName("");
        paramPost.setQuery(query);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonString;

        try {
            jsonString = ow.writeValueAsString(paramPost);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(datahubHostFrontend + "/api/graphql"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", String.format("Bearer %s", token))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SearchUrn.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "not_found";
    }

    public Map<String, String> checkProperties(String urn, String datahubHostFrontend, String datahubHostGMS, String token, String airflowTriggerInterval) throws IOException, InterruptedException {
        String regexPattern = "(?<=airflow,)(.*?)(?=,prod)";
                
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcherAirflowDAG = pattern.matcher(urn);
        Map<String, String> resultDatahub;
        if (matcherAirflowDAG.find()) {
              String airflowUrn = String.format("urn:li:dataFlow:(airflow,%s,prod)",matcherAirflowDAG.group(1));
               resultDatahub = MiddlewareHelper.updateToDatahub(datahubHostGMS, datahubHostFrontend, token,airflowUrn, urn,airflowTriggerInterval);
         } else {
             resultDatahub = MiddlewareHelper.updateToDatahub(datahubHostGMS, datahubHostFrontend, token,urn, urn,airflowTriggerInterval);
        }
      
        if (resultDatahub == null) {
            return null;
        } else {
            return resultDatahub;
        }

    }

    private static String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    public Map<String, String> checkThresholdDataFlow(String urn, String datahubHostFrontend, String datahubHostGMS, String token) {
        try {
            Proposal proposal = new Proposal();
            proposal.setEntityType("dataFlow");
            proposal.setEntityUrn(urn);
            proposal.setChangeType("UPSERT");
            proposal.setAspectName("dataFlowInfo");

            Aspect aspect = new Aspect();
            Map<String, Object> value = new HashMap<>();

            Gson gson = new GsonBuilder().disableHtmlEscaping()
                    .create();

            Map<String, String> customProperties = new HashMap<>();

            HttpClient client = HttpClient.newHttpClient();

            Map<String, String> responseDatahub = new HashMap<>();

            String encodedUrn = encodeValue(urn);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(datahubHostGMS + "/entitiesV2/" + encodedUrn))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//             System.out.println(response.body());
            if (response == null) {
                responseDatahub.put("object_status", "not_found");
                responseDatahub.put("error", "GMS not active");
            }

            JSONObject jsonObject = new JSONObject(response.body());
            JSONObject aspects = jsonObject.getJSONObject("aspects");
            JSONObject dataFlowInfo = aspects.getJSONObject("dataFlowInfo");
            JSONObject customProp = dataFlowInfo.getJSONObject("value").getJSONObject("customProperties");

            Map<String, String> customPropertiesMap = new HashMap<>();
            OffsetDateTime offsetDateTime = OffsetDateTime.now(ZoneId.of("Asia/Jakarta"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            customProp.keys().forEachRemaining(key -> {
//                System.out.println(key);
                if (!key.contains("openlineage_") && !key.contains("doc_md")) {
                    if (key.equals("evd_last_ingestion")) {

                        if (responseDatahub.containsKey("evd_last_ingestion_temp")) {
                            responseDatahub.put("evd_last_ingestion_temp", OffsetDateTime.parse(customProp.getString("evd_last_ingestion_temp"), DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(formatter));
                        }
                        
                        responseDatahub.put("evd_last_ingestion", offsetDateTime.format(formatter));

                        customPropertiesMap.put("evd_last_ingestion_temp", customProp.getString("evd_last_ingestion"));
                        customPropertiesMap.put("evd_last_ingestion", offsetDateTime.format(formatter));

                    } else {
                        String values = customProp.getString(key);
                        customPropertiesMap.put(key, values);
                    }
                }
            });

            customPropertiesMap.put("evd_last_ingestion", offsetDateTime.format(formatter));

            dataFlowInfo.getJSONObject("value").keys().forEachRemaining(key -> {
                if (!key.contentEquals("description") && !key.contentEquals("customProperties")) {
                    value.put(key, dataFlowInfo.getJSONObject("value").get(key));
                }

            });

            value.put("customProperties", customPropertiesMap);
            aspect.setValue(gson.toJson(value));
            aspect.setContentType("application/json");

            proposal.setAspect(aspect);
            ProposalWrapper pw = new ProposalWrapper();
            pw.setProposal(proposal);
            String jsonString = gson.toJson(pw).replace("\\\\\\", "\\");

//        System.out.println(jsonString);
            client = HttpClient.newHttpClient();

            request = HttpRequest.newBuilder()
                    .uri(URI.create(datahubHostGMS + "/aspects?action=ingestProposal"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", String.format("Bearer %s", token))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                    .build();
            try {
                HttpResponse<String> responses = client.send(request, HttpResponse.BodyHandlers.ofString());
                responseDatahub.put("responseDatahub", response.body());
                return responseDatahub;
            } catch (Exception x) {
                System.out.println(x.getMessage());
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (InterruptedException ex) {
            Logger.getLogger(BatchHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
