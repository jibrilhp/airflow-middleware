package jibs.middleware.api.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
//import io.micronaut.http.client.annotation.Client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Controller("/hello") // (1)
class TestController {
        @Get(produces = MediaType.TEXT_PLAIN) // (2)
        public String index() throws IOException, InterruptedException{

            String graphqlQuery =     "mutation updateLineage {\\n" +
                    "                    updateLineage(\\n" +
                    "                        input: {\\n" +
                    "                        edgesToAdd: [\\n" +
                    "                            {\\n                            " +
                    "downstreamUrn: \\\"urn:li:dataJob:(urn:li:dataFlow:(airflow,datamart_holiday_daily,prod),params_eval)\\\"\\n                            " +
                    "upstreamUrn: \\\"urn:li:dataset:(urn:li:dataPlatform:bigquery,data-bi-dev-365405.dimensions._nopal,PROD)\\\"\\n" +
                    "                            " +
                    "}\\n                        " +
                    "]\\n                        " +
                    "edgesToRemove: []\\n                        " +
                    "}\\n                      " +
                    ")\\n                    " +
                    "}\\n";


            HttpClient client = HttpClient.newHttpClient();


            // Set up the HttpRequest with POST method and attach the GraphQL query
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/graphql"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhY3RvclR5cGUiOiJVU0VSIiwiYWN0b3JJZCI6ImRhdGFodWIiLCJ0eXBlIjoiUEVSU09OQUwiLCJ2ZXJzaW9uIjoiMiIsImp0aSI6IjM0Y2NkY2Q2LWY5ZjAtNDE4YS1hZDRiLTE1OGRlYWYyMmI0MCIsInN1YiI6ImRhdGFodWIiLCJpc3MiOiJkYXRhaHViLW1ldGFkYXRhLXNlcnZpY2UifQ.f5j5OuOTj1Zifj07KYVObC1gYSeW_0X3CeyEa7GtU5g")
//                    .POST(HttpRequest.BodyPublishers.ofString("{\"operationName\":\"searchAcrossLineage\",\"query\":\"" + graphqlQuery + "\"}"))
                    .POST(HttpRequest.BodyPublishers.ofString("{\"query\":\"" + graphqlQuery +" \"" +
                            ",\"operationName\":\"updateLineage\"}"))

                    .build();

            // Send the POST request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response
            System.out.println(response.body());
            return response.body(); // (3)
    }
}



