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
//    // Podstawowy URL do API MODIS
//    private final String modisApiUrl = "https://modis.ornl.gov/rst/api/v1/";
//
//    @GetMapping("/modis")
//    public ResponseEntity<Map<String, Object>> getModisData(
//            @RequestParam String username,
//            @RequestParam String password) {
//
//        // Zastąp "your_endpoint" odpowiednim endpointem, np. "MODIS/Terra"
//        String endpoint = "MODIS/Terra";  // Możesz zmienić ten endpoint w zależności od potrzeb
//        String url = modisApiUrl + endpoint;
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpGet request = new HttpGet(url);
//            // Ustawienie nagłówka autoryzacji
//            request.setHeader("Authorization", "Basic " +
//                    java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
//
//            CloseableHttpResponse response = httpClient.execute(request);
//            HttpEntity entity = response.getEntity();
//
//            if (entity != null) {
//                String result = EntityUtils.toString(entity);
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode jsonNode = mapper.readTree(result);
//
//                Map<String, Object> modisData = new HashMap<>();
//                modisData.put("data", jsonNode);  // Zwracamy dane w formacie JSON
//
//                return ResponseEntity.ok(modisData);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//    }
//}
