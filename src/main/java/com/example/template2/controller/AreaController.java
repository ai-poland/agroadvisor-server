package com.example.template2.controller;

import com.example.template2.model.Area;
import com.example.template2.model.AreaRepository;
import com.example.template2.model.SimpleArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/db")
public class AreaController {


    private final AreaRepository areaRepository;


    @Autowired
    public AreaController(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }


    @PostMapping("/area")
    public ResponseEntity<Area> addArea(@RequestBody Area area)
    {
        return new ResponseEntity<>(areaRepository.save(area), HttpStatus.CREATED);
    }


    @GetMapping(path = "/area/user/{login}")
    public ResponseEntity<Object> getAllByLogin(@PathVariable String login)
    {
        if (areaRepository.existsByLogin(login))
        {
            List<SimpleArea> area = areaRepository.getAllLessDetailed(login);
            return ResponseEntity.ok(area);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping(path = "/area/{id}")
    public ResponseEntity<Object> getAreas(@PathVariable int id) {
        if (areaRepository.existsById(id))
        {
            Optional<Area> area = areaRepository.findById(id);
            return ResponseEntity.ok(area);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
}