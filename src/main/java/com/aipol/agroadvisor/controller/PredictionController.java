package com.aipol.agroadvisor.controller;

import com.aipol.agroadvisor.model.Prediction;
import com.aipol.agroadvisor.service.PredictionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/db/prediction")
public class PredictionController {
    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping("/")
    public List<Prediction> getPredictions() {
        return this.predictionService.getPredictions();
    }

    @GetMapping("/{areaId}")
    public ResponseEntity<Object> getPredictionByAreaId(@PathVariable int areaId) {
        return this.predictionService.getPredictionByAreaId(areaId);
    }

    //Do testowania POST requestów
    /*@PostMapping("/")
    public ResponseEntity<Object> createPrediction(@RequestBody Prediction prediction) {
        return this.predictionService.createPrediction(prediction);
    }*/
}
