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
package jibs.middleware.api.datahub;

import jibs.middleware.api.parameters.datahub.DatahubParameters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jibrilhp
 */
public class SearchUrn {
    public String Search(String project, String dataset, String table, String datahubHostFrontend, String token) throws InterruptedException, IOException {
         HttpClient client = HttpClient.newHttpClient(); 
        
        String projectDataset = project + "." + dataset;
        String query = String.format("""
        query searchAcrossLineage {
          searchAcrossLineage(
            input: {
              query: "*"
              urn: "urn:li:dataset:(urn:li:dataPlatform:bigquery,%s.%s,PROD)"
              start: 0
              count: 1000
              direction: DOWNSTREAM
              orFilters: [
                {
                  and: [
                    {
                      condition: EQUAL
                      negated: false
                      field: "degree"
                      values: ["1"] # ["1", "2", "3+"]
                    }
                  ]
                }
              ]
            }
          ) {
            searchResults {
              degree
              entity {
                urn
                type
              }
            }
          }
        }
        """, projectDataset,table);
        
        System.out.println(query);
      
        DatahubParameters paramPost = new DatahubParameters();
        paramPost.setOperationName("searchAcrossLineage");
        paramPost.setQuery(query);
        
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonString;
        try {
            jsonString = ow.writeValueAsString(paramPost);
            
             HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(datahubHostFrontend + "/api/graphql"))
            .header("Content-Type", "application/json")
            .header("Authorization", String.format("Bearer %s",token))
            .POST(HttpRequest.BodyPublishers.ofString(jsonString))

            .build();

      
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
//            System.out.println(response.body());
            return response.body();
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SearchUrn.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
       
    }
}
