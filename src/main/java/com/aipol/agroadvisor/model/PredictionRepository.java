package com.aipol.agroadvisor.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionRepository extends JpaRepository<Prediction, Integer> {
}
