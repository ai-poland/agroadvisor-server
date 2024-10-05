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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
public class RainController {

    @RequestMapping("/rain")
    public ResponseEntity<JsonNode> getRainfallData(@RequestParam double latitude,
                                                    @RequestParam double longitude,
                                                    @RequestParam String date) {
        LocalDate requestedDate;

        // Parse the incoming date string
        try {
            requestedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Invalid date format
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // Construct the API URL with the requested date
        String url = "https://power.larc.nasa.gov/api/temporal/daily/point?parameters=PRECTOTCORR&community=AG" +
                "&longitude=" + longitude + "&latitude=" + latitude +
                "&start=" + requestedDate.format(formatter) +
                "&end=" + requestedDate.format(formatter) + "&format=JSON";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(result);

                    JsonNode rainfallData = jsonNode.path("properties").path("parameter").path("PRECTOTCORR");
                    if (!rainfallData.isMissingNode()) {
                        // Convert rainfall data to timestamp format if needed
                        // Here we can assume you might want to wrap the data in a timestamp node
                        JsonNode responseData = mapper.createObjectNode()
                                .put("timestamp", requestedDate.toString())
                                .set("rainfall", rainfallData);
                        return ResponseEntity.ok(responseData);
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
