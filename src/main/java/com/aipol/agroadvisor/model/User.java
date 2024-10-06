package com.aipol.agroadvisor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    private String login;
    private String name;
    private String password;
}
