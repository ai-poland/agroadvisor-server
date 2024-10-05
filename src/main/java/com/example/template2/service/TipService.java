package com.example.template2.service;

import com.example.template2.model.Area;
import com.example.template2.model.Tip;
import com.example.template2.model.TipRepository;
import com.example.template2.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TipService {

    private final TipRepository tipRepository;

    public TipService(TipRepository tipRepository) {
        this.tipRepository = tipRepository;
    }


    public ResponseEntity<Object> deleteTipById(int id) {
        Optional<Tip> optionalArea = tipRepository.findById(id);

        if(!optionalArea.isPresent()){
            return ResponseEntity.notFound().build();
        }

        tipRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<Object> generateTip() {
        Tip tip = new Tip();
        // do something here
        return new ResponseEntity<>(tip, HttpStatus.CREATED);
    }

}
