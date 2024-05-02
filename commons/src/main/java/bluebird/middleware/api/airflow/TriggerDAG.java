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
package jibs.middleware.api.airflow;

import jibs.middleware.api.datahub.SearchUrn;
import jibs.middleware.api.airflow.AirflowParameters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import jibs.middleware.api.helper.MiddlewareHelper;
import java.util.Map;

/**
 *
 * @author jibrilhp
 */
public class TriggerDAG {
     public static String Trigger(String urn, String urnFull, String dagName, String airflowHost, String airflowToken, String hostGms, String hostFrontend, String datahubToken, String airflowTriggerInterval) throws InterruptedException, IOException {
        HttpClient client = HttpClient.newHttpClient(); 
        
        // Check threshold in datahub 
        
        Map<String,String> resultDatahub = MiddlewareHelper.updateToDatahub(hostGms,hostFrontend,datahubToken, urn,urnFull, airflowTriggerInterval);
//        System.out.println(resultDatahub);
        if (resultDatahub == null) {
            return null;
        }
        
        String resultMap = resultDatahub.get("object_status");
        if (resultMap.equals("success")) {
        
            AirflowParameters paramPost = new AirflowParameters();
            Conf configAirflow = new Conf();
            
            
            configAirflow.setStart_date(resultDatahub.get("evd_last_ingestion_temp"));
            configAirflow.setEnd_date(resultDatahub.get("evd_last_ingestion"));

            paramPost.setConf(configAirflow);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String jsonString = "";
            try {
                jsonString = ow.writeValueAsString(paramPost);

                 HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(airflowHost + String.format("/api/v1/dags/%s/dagRuns",dagName)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + airflowToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))

                .build();


                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
//                System.out.println(response.body());
      
                return response.body();
            } catch (JsonProcessingException ex) {
                Logger.getLogger(SearchUrn.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (resultMap.equals("wait_for_schedule")) {
            return "{\"status\":\"Waiting..\"}";
        }
       
        return null;
       
    }
     
     
      public static String TriggerDaily(String urn, String dagName, String airflowHost, String airflowToken, String hostGms, String hostFrontend, String datahubToken, Map<String,String> resultDatahub) throws InterruptedException, IOException {
        HttpClient client = HttpClient.newHttpClient(); 
        
       
        
          AirflowParameters paramPost = new AirflowParameters();
          Conf configAirflow = new Conf();

          configAirflow.setStart_date(resultDatahub.get("evd_last_ingestion_temp"));
          configAirflow.setEnd_date(resultDatahub.get("evd_last_ingestion"));

          paramPost.setConf(configAirflow);

          ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
          String jsonString = "";
          try {
              jsonString = ow.writeValueAsString(paramPost);

              HttpRequest request = HttpRequest.newBuilder()
                      .uri(URI.create(airflowHost + String.format("/api/v1/dags/%s/dagRuns", dagName)))
                      .header("Content-Type", "application/json")
                      .header("Authorization", "Basic " + airflowToken)
                      .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                      .build();

              HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//                System.out.println(response.body());
              return response.body();
          } catch (JsonProcessingException ex) {
              Logger.getLogger(SearchUrn.class.getName()).log(Level.SEVERE, null, ex);
          }

     
       
        return null;
       
    }
    
}
