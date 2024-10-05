package com.example.template2.controller;

import com.example.template2.service.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/db")
public class TipController {

    private final TipService tipService;


    @Autowired
    public TipController(TipService tipService) {
        this.tipService = tipService;
    }

    @DeleteMapping("/tip/{id}")
    public ResponseEntity<Object> deleteAreaById(@PathVariable int id) {
        return ResponseEntity.ok(this.tipService.deleteTipById(id));
    }

    @GetMapping("/tip/generate/{id}")
    public ResponseEntity<Object> generateByAreaId(@PathVariable int id) {
        return ResponseEntity.ok(this.tipService.generateTip(id));
    }



}
