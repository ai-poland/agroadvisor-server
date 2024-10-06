package com.aipol.agroadvisor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class GPT {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "";
    public static String callOpenAI(String system, String user) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        String body ="{\n" +
                "    \"model\": \"gpt-3.5-turbo\",\n" +
                "    \"messages\": [\n" +
                "        {\"role\": \"system\", \"content\": \""+ system +"\"},\n" +
                "        {\"role\": \"user\", \"content\": \"" + user + "\"}\n" +
                "    ],\n" +
                "    \"max_tokens\": 100\n" +
                "}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class).getBody();
    }
}
