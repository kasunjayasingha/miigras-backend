package com.kasunjay.miigrasbackend.repository;

import com.kasunjay.miigrasbackend.entity.mobile.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PredictionRepo extends JpaRepository<Prediction, Long> {

    List<Prediction> findByIsCheckFalseOrderByScoreDescCreatedDateDesc();

    long countPredictionByCreatedDateBetweenAndIsCheckFalse(LocalDateTime oneMonthBefore, LocalDateTime currentDate);

    long countPredictionByIsCheckFalse();
}
