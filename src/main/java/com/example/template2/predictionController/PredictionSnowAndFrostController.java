package com.example.template2.predictionController;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PredictionSnowAndFrostController {

    @Value("${openweather.api.key}")
    private String openWeatherApiKey;

    @GetMapping("/predictionSnowAndFrost")
    public ResponseEntity<?> getPredictionSnowAndFrost(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric", latitude, longitude, openWeatherApiKey);
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Request snow and frost data from OpenWeather API
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> data = objectMapper.readValue(response.getBody(), HashMap.class);
                Map<String, Object> mainData = (Map<String, Object>) data.get("main");

                // Prepare prediction response
                Map<String, Object> prediction = new HashMap<>();
                prediction.put("latitude", latitude);
                prediction.put("longitude", longitude);
                prediction.put("snow", data.get("snow") != null ? data.get("snow") : "No snow forecast");
                prediction.put("frost", mainData.get("temp_min") != null && (double) mainData.get("temp_min") < 0 ? "Frost expected" : "No frost expected");
                prediction.put("start_date", startDate);
                prediction.put("end_date", endDate);

                return ResponseEntity.ok(prediction);
            } else {
                // If location is not found, return a 404
                Map<String, String> error = new HashMap<>();
                error.put("error", "Location not found.");
                return ResponseEntity.status(404).body(error);
            }
        } catch (Exception e) {
            // Handle any exceptions
            Map<String, String> error = new HashMap<>();
            error.put("error", "An error occurred while fetching data.");
            return ResponseEntity.status(500).body(error);
        }
    }
}