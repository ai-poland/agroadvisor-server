//package com.example.template2.service;
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
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//public class WeatherForecastService {
//
//    @GetMapping("/precipitationData")
//    public ResponseEntity<Map<String, Object>> getPrecipitationData(
//            @RequestParam double longitude,
//            @RequestParam double latitude,
//            @RequestParam String startDate,  // Dodany parametr startDate
//            @RequestParam String endDate) {  // Dodany parametr endDate
//
//        // URL z parametrem opadów (np. "PRECTOTCORR") dla opadów
//        String url = "https://power.larc.nasa.gov/api/temporal/daily/point?parameters=PRECTOTCORR&community=AG" +
//                "&longitude=" + longitude + "&latitude=" + latitude + "&start=" + startDate + "&end=" + endDate + "&format=JSON";
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpGet request = new HttpGet(url);
//            try (CloseableHttpResponse response = httpClient.execute(request)) {
//                HttpEntity entity = response.getEntity();
//
//                if (entity != null) {
//                    String result = EntityUtils.toString(entity);
//                    System.out.println("Full API Response: " + result); // Log pełnej odpowiedzi
//                    ObjectMapper mapper = new ObjectMapper();
//                    JsonNode jsonNode = mapper.readTree(result);
//
//                    // Wyciągnięcie danych o opadach (PRECTOTCORR)
//                    JsonNode precipitationData = jsonNode.path("properties").path("parameter").path("PRECTOTCORR");
//
//                    if (!precipitationData.isMissingNode() && precipitationData.isObject()) {
//                        List<Double> precipitationList = new ArrayList<>();
//                        precipitationData.fieldNames().forEachRemaining(timestamp -> {
//                            precipitationList.add(precipitationData.get(timestamp).asDouble());
//                        });
//
//                        // Obliczenie prognozowanych opadów na następne 7 dni
//                        double[] predictions = predictFutureRainfall(precipitationList.stream().mapToDouble(Double::doubleValue).toArray(), 365);
//
//                        // Przygotowanie wyniku
//                        Map<String, Object> responseMap = new HashMap<>();
//                        responseMap.put("predictedPrecipitation", predictions);
//                        responseMap.put("originalPrecipitationData", precipitationList);
//
//                        return ResponseEntity.ok(responseMap);
//                    } else {
//                        System.err.println("Precipitation data is missing");
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//                    }
//                } else {
//                    System.err.println("Entity is null");
//                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    // Metoda do przewidywania opadów na podstawie regresji liniowej
//    private double[] predictFutureRainfall(double[] pastRainfall, int daysToPredict) {
//        int n = pastRainfall.length;
//
//        // Obliczanie sum
//        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
//        for (int i = 0; i < n; i++) {
//            sumX += (i + 1); // Czas (dzień)
//            sumY += pastRainfall[i]; // Wartość opadów
//            sumXY += (i + 1) * pastRainfall[i];
//            sumX2 += (i + 1) * (i + 1);
//        }
//
//        // Obliczanie nachylenia (m) i wyrazu wolnego (b) prostej regresji
//        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
//        double intercept = (sumY - slope * sumX) / n;
//
//        // Przewidywanie opadów na określoną liczbę dni
//        double[] predictedRainfall = new double[daysToPredict];
//        for (int i = 0; i < daysToPredict; i++) {
//            predictedRainfall[i] = slope * (n + i + 1) + intercept; // Przewidywanie na dzień (n + i + 1)
//        }
//
//        // Zaokrąglenie wartości do dwóch miejsc po przecinku
//        for (int i = 0; i < predictedRainfall.length; i++) {
//            predictedRainfall[i] = Math.round(predictedRainfall[i] * 100.0) / 100.0;
//        }
//
//        return predictedRainfall;
//    }
//
//}
