package com.example.template2.controller;

import com.example.template2.service.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
