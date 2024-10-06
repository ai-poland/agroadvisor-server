package com.aipol.agroadvisor.service;

import com.aipol.agroadvisor.model.Area;
import com.aipol.agroadvisor.model.AreaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AreaService {
    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    public ResponseEntity<Object> deleteAreaById(int id) {
        Optional<Area> optionalArea = this.areaRepository.findById(id);

        if(!optionalArea.isPresent()){
            return ResponseEntity.notFound().build();
        }

        areaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
