package com.aipol.agroadvisor.service;

import com.example.template2.GPT;
import com.aipol.agroadvisor.model.AreaRepository;
import com.aipol.agroadvisor.model.Tip;
import com.aipol.agroadvisor.model.TipRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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

        String body;

        body = GPT.callOpenAI("", "");
        tip.setContent(body);

        return new ResponseEntity<>(tip, HttpStatus.CREATED);
    }
}
