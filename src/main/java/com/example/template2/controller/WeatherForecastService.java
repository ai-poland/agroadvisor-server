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
import java.util.HashMap;
import java.util.Map;

@RestController
public class WeatherForecastService {

    private final String apiKey = "d3dac631ab0ded23d77c3597b162a860";

    @GetMapping("/weather")
    public ResponseEntity<Map<String, Object>> getWeatherForecast(
            @RequestParam double latitude,
            @RequestParam double longitude) {

        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude
                + "&lon=" + longitude + "&units=metric&appid=" + apiKey;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String result = EntityUtils.toString(entity);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(result);
                JsonNode list = jsonNode.path("list");

                Map<String, Object> forecastData = new HashMap<>();
                for (JsonNode node : list) {
                    long dateTime = node.path("dt").asLong() * 1000; // Timestamp
                    double temperature = node.path("main").path("temp").asDouble();
                    String weatherDescription = node.path("weather").get(0).path("description").asText();

                    Map<String, Object> dayData = new HashMap<>();
                    dayData.put("temperature", temperature);
                    dayData.put("description", weatherDescription);

                    forecastData.put(String.valueOf(dateTime), dayData);
                }

                return ResponseEntity.ok(forecastData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
