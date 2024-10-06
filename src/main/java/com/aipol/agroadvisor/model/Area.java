package com.aipol.agroadvisor.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "area")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String user_login;

    @Column(nullable = false)
    private String location;

    private Timestamp last_generated_tip;
}