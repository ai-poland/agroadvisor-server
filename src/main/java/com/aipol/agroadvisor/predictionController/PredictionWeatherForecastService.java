package com.aipol.agroadvisor.predictionController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Value;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PredictionWeatherForecastService {

    @Value("${openweather.api.key}")
    private String openWeatherApiKey;

    @GetMapping("/weatherForecast")
    public ResponseEntity<?> getWeatherForecast(
            @RequestParam double latitude,
            @RequestParam double longitude) {

        String url = String.format("https://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=metric&appid=%s", latitude, longitude, openWeatherApiKey);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(result);
                    JsonNode list = jsonNode.path("list");

                    Map<String, Object> forecastData = new HashMap<>();

                    for (JsonNode node : list) {
                        String dateTime = node.path("dt_txt").asText();
                        double temperature = node.path("main").path("temp").asDouble();
                        String weatherDescription = node.path("weather").get(0).path("description").asText();

                        Map<String, Object> dayData = new HashMap<>();
                        dayData.put("temperature", temperature);
                        dayData.put("description", weatherDescription);

                        forecastData.put(dateTime, dayData);
                    }

                    return ResponseEntity.ok(forecastData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "An error occurred while fetching data.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

        Map<String, String> error = new HashMap<>();
        error.put("error", "No data available.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}