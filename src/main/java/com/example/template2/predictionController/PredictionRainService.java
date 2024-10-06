
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
public class PredictionRainService {

    @Value("${openweather.api.key}")
    private String openWeatherApiKey;

    @GetMapping("/predictionRain")
    public ResponseEntity<?> getPredictionRain(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam String day) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric", latitude, longitude, openWeatherApiKey);
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Request weather data from OpenWeather
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> data = objectMapper.readValue(response.getBody(), HashMap.class);
                Map<String, Object> mainData = (Map<String, Object>) data.get("main");
                Map<String, Object> rainData = (Map<String, Object>) data.get("rain");

                // Prepare prediction response
                Map<String, Object> prediction = new HashMap<>();
                prediction.put("latitude", latitude);
                prediction.put("longitude", longitude);
                prediction.put("temperature", mainData.get("temp"));
                prediction.put("humidity", mainData.get("humidity"));
                prediction.put("rain_prediction", rainData != null ? rainData.toString() : "No rain forecast");
                prediction.put("day", day);

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