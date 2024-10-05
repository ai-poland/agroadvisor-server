//package com.example.template2.controller;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//public class VegetationGrowthController {
//
//    // Base URL for OpenWeatherMap Free API
//    private final String openWeatherApiUrl = "https://api.openweathermap.org/data/2.5/weather";
//
//    @GetMapping("/weather")
//    public ResponseEntity<Map<String, Object>> getWeatherData(
//            @RequestParam double latitude,
//            @RequestParam double longitude,
//            @RequestParam String apiKey) {
//
//        String url = String.format("%s?lat=%s&lon=%s&appid=%s&units=metric", openWeatherApiUrl, latitude, longitude, apiKey);
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpGet request = new HttpGet(url);
//
//            try (CloseableHttpResponse response = httpClient.execute(request)) {
//                HttpEntity entity = response.getEntity();
//
//                if (entity != null) {
//                    String result = EntityUtils.toString(entity);
//                    ObjectMapper mapper = new ObjectMapper();
//                    JsonNode jsonNode = mapper.readTree(result);
//
//                    Map<String, Object> weatherData = new HashMap<>();
//                    weatherData.put("data", jsonNode);  // Returning data in JSON format
//
//                    return ResponseEntity.ok(weatherData);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Map<String, Object> error = new HashMap<>();
//            error.put("error", "An error occurred while fetching weather data.");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//        }
//
//        Map<String, Object> error = new HashMap<>();
//        error.put("error", "No data available.");
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }
//}