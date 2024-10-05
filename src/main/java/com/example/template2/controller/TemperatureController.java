package com.example.template2.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TemperatureController {

    @GetMapping("/temperatureOn2Meters")
    public ResponseEntity<JsonNode> getTemperatureData(
            @RequestParam double longitude,
            @RequestParam double latitude,
            @RequestParam String startDate,  // Dodany parametr startDate
            @RequestParam String endDate) {  // Dodany parametr endDate

        // URL z parametrem T2M dla temperatury powietrza
        String url = "https://power.larc.nasa.gov/api/temporal/daily/point?parameters=T2M&community=AG" +
                "&longitude=" + longitude + "&latitude=" + latitude + "&start=" + startDate + "&end=" + endDate + "&format=JSON";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    System.out.println("Full API Response: " + result); // Log pełnej odpowiedzi
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(result);

                    // Wyciągnięcie danych o temperaturze powietrza (T2M)
                    JsonNode temperatureData = jsonNode.path("properties").path("parameter").path("T2M");

                    if (!temperatureData.isMissingNode() && temperatureData.isObject()) {
                        ObjectNode combinedData = mapper.createObjectNode();
                        temperatureData.fieldNames().forEachRemaining(timestamp -> {
                            combinedData.put(timestamp, temperatureData.get(timestamp).asDouble());
                        });
                        // Zwrócenie odpowiedzi
                        return ResponseEntity.ok(combinedData);
                    } else {
                        System.err.println("Temperature data is missing");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                    }
                } else {
                    System.err.println("Entity is null");
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}