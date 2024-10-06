package com.aipol.agroadvisor.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "prediction")
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private PredictionType type;
    private int value;
    private int timestamp;
    private int areaId;
}
