package com.example.template2.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class SoilMoistureController {

    @GetMapping("/SoilMoisture")
    public ResponseEntity<Map<String, Double>> getSoilMoistureData(
            @RequestParam double longitude,
            @RequestParam double latitude) {

        String url = "https://power.larc.nasa.gov/api/temporal/daily/point?parameters=GWETROOT,PRECTOT&community=AG&longitude="
                + longitude + "&latitude=" + latitude + "&start=20220101&end=20220131&format=JSON";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String result = EntityUtils.toString(entity);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(result);
                JsonNode soilMoistureData = jsonNode.path("properties").path("parameter").path("GWETROOT");

                // Use TreeMap for automatic sorting by date
                Map<String, Double> timestampData = new TreeMap<>();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

                // Convert data to timestamp
                soilMoistureData.fieldNames().forEachRemaining(date -> {
                    try {
                        long timestamp = sdf.parse(date).getTime(); // Convert date to timestamp
                        double moisture = soilMoistureData.path(date).asDouble(0); // Change to Double
                        timestampData.put(String.valueOf(timestamp), moisture); // Store in the TreeMap
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                });

                return ResponseEntity.ok(timestampData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
