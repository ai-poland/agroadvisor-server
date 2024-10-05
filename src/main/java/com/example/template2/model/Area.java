package com.example.template2.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

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
}