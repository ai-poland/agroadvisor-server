package com.example.template2.service;

import com.example.template2.model.Prediction;
import com.example.template2.model.PredictionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PredictionService {
    private final PredictionRepository predictionRepository;

    public PredictionService(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }

    public List<Prediction> getPredictions() {
        return this.predictionRepository.findAll();
    }

    public ResponseEntity<Object> getPredictionByAreaId(int areaId) {
        Optional<Prediction> optionalPrediction = this.predictionRepository.findById(areaId);

        if(!optionalPrediction.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Prediction prediction = optionalPrediction.get();
        return ResponseEntity.ok(prediction);
    }

    //Do testowania POST requestów
    /*public ResponseEntity<Object> createPrediction(Prediction prediction) {
        predictionRepository.save(prediction);
        return new ResponseEntity<>(prediction, HttpStatus.CREATED);
    }*/
}
