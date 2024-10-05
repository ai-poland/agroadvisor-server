package com.example.template2.controller;

import com.example.template2.model.Area;
import com.example.template2.model.AreaRepository;
import com.example.template2.model.SimpleArea;
import com.example.template2.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/db")
public class AreaController {


    private final AreaRepository areaRepository;
    private final AreaService areaService;
    private final MessageDigest messageDigest;

    @Autowired
    public AreaController(AreaRepository areaRepository, AreaService areaService) throws NoSuchAlgorithmException {
        this.areaRepository = areaRepository;
        this.areaService = areaService;
        this.messageDigest = MessageDigest.getInstance("SHA-256");
    }

    private String hashPassword(String password) {
        byte[] hashedBytes = messageDigest.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
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
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/area/{id}")
    public ResponseEntity<Object> deleteAreaById(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();
        String currentUserPassword = authentication.getCredentials().toString();

        String hashedPassword = hashPassword(currentUserPassword);
        Optional<Area> optionalArea = this.areaRepository.findByIdAndUserLogin(id, currentUserLogin, hashedPassword);

        if (!optionalArea.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        areaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }



}