package com.example.template2.model;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tip")
public class Tip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String content;
    private Timestamp add_time = new Timestamp(System.currentTimeMillis());


    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Area area;
}
