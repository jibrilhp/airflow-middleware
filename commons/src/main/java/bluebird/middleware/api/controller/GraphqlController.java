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

import jibs.middleware.api.parameters.datahub.DatahubParameters;
import jibs.middleware.api.parameters.stream.StreamParameters;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.logging.Level;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jibrilhp
 */
@Controller("/api/graphql")
@Singleton
public class GraphqlController {

    private final String token;
    private final String datahubHostFrontend;
    private final String datahubHostGms;

    public GraphqlController(@Value("${datahub.token}") String token, @Value("${datahub.host-frontend}") String datahubHostFrontend, @Value("${datahub.host-gms}") String datahubHostGms, @Value("${airflow.trigger-interval}") String airflowTriggerInterval, @Value("${airflow.host}") String airflowHost, @Value("${airflow.authorization}") String airflowToken) {
        this.token = token;
        this.datahubHostFrontend = datahubHostFrontend;
        this.datahubHostGms = datahubHostGms;
    }

    private static final Logger L = LoggerFactory.getLogger(GraphqlController.class);

    @Post(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> execute(@Body DatahubParameters data) throws IOException, InterruptedException {
        
        String operationName = data.getOperationName();
        String query = data.getQuery();
        
       Thread jobIngestion = new Thread() {
            public void run() {
                try {
                    
                  
                    Thread.sleep(60000);
                    // call an update 
                    
                    HttpClient client = HttpClient.newHttpClient();
                    
                    JSONObject postData = new JSONObject();
                    postData.put("operationName", operationName);
                    postData.put("query", query);
                    
                     String postObject = postData.toString();
                     L.info("Request query: " + postObject);

                    // Set up the HttpRequest with POST method and attach the GraphQL query
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(datahubHostFrontend + "/api/graphql"))
                            .header("Content-Type", "application/json")
                            .header("Authorization", "Bearer " + token)
                            .POST(HttpRequest.BodyPublishers.ofString(postObject))

                            .build();

                    // Send the POST request
                    java.net.http.HttpResponse<String> response;
                  
                    try {
                        response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
                          L.info("Response from server: " + response.body());
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(GraphqlController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // Print the response
                   
                    
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        };
       
       jobIngestion.start();
       L.info("Thread started and waiting for 60s - " + String.valueOf(jobIngestion.getId()));

       return HttpResponse.ok("{}");
    }

}
