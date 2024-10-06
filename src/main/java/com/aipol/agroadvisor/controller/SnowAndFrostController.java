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
public class SnowAndFrostController {

    @GetMapping("/SnowAndFrost")
    public ResponseEntity<Map<String, Double>> getSnowData(
            @RequestParam double longitude,
            @RequestParam double latitude,
            @RequestParam String startDate,  // Dodany parametr startDate
            @RequestParam String endDate) {  // Dodany parametr endDate

        String url = "https://power.larc.nasa.gov/api/temporal/daily/point?parameters=PRECTOTCORR&community=AG&longitude="
                + longitude + "&latitude=" + latitude + "&start=" + startDate + "&end=" + endDate + "&format=JSON"; // dynamiczne daty

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String result = EntityUtils.toString(entity);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(result);
                JsonNode precipitationData = jsonNode.path("properties").path("parameter").path("PRECTOTCORR");

                // Użycie TreeMap do automatycznego sortowania po dacie
                Map<String, Double> timestampData = new TreeMap<>();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

                // Konwertuj dane na timestamp
                precipitationData.fieldNames().forEachRemaining(date -> {
                    try {
                        long timestamp = sdf.parse(date).getTime(); // konwersja daty na timestamp
                        double precipitation = precipitationData.path(date).asDouble(0); // zmiana na Double
                        timestampData.put(String.valueOf(timestamp), precipitation); // Zmiana na Double
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
