package com.example.template2.service;

import com.example.template2.model.AreaRepository;
import com.example.template2.model.Tip;
import com.example.template2.model.TipRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
public class TipService {

    private final TipRepository tipRepository;
    private final AreaRepository areaRepository;

    public TipService(TipRepository tipRepository, AreaRepository areaRepository) {
        this.tipRepository = tipRepository;
        this.areaRepository = areaRepository;
    }


    public ResponseEntity<Object> deleteTipById(int id) {
        Optional<Tip> optionalArea = tipRepository.findById(id);

        if(!optionalArea.isPresent()){
            return ResponseEntity.notFound().build();
        }

        tipRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<Object> generateTip(int area_id) {
        Tip tip = new Tip();

        if (!areaRepository.existsById(area_id))
        {
            return ResponseEntity.badRequest().build();
        }

        tip.setArea(areaRepository.findById(area_id).get());

        GPT gpt = new GPT("Podaj mi krótkie przewidywanie dotyczące pogody w Warszawie na najbliższ dzień.");

        String body;

        try {
            body = gpt.getResponse().body();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

        tip.setContent(body);

        return new ResponseEntity<>(tip, HttpStatus.CREATED);
    }


    private class GPT
    {
        private final String body;
        private final HttpClient client;
        private final HttpRequest request;

        public GPT(String prompt)
        {
            String apiKey="";
            body = """
                {
                    "model": "gpt-4o",
                    "messages": [
                        {
                            "role": "user",
                            "content": 
                            """+
            prompt+
                    """
                        }
                    ]
                }""";

            client = HttpClient.newHttpClient();
            request = HttpRequest.newBuilder().uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
        }

        public HttpResponse<String> getResponse()throws IOException, InterruptedException
        {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
    }



}
