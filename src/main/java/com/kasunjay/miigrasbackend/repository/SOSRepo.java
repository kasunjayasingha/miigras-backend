package com.kasunjay.miigrasbackend.repository;

import com.kasunjay.miigrasbackend.entity.mobile.SOS;
import com.kasunjay.miigrasbackend.entity.web.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SOSRepo extends JpaRepository<SOS, Long> {
    Optional<SOS> findByEmployeeAndActive(Employee employee, Boolean active);

    long countSOSByCreatedDateBetween(LocalDateTime oneMonthBefore, LocalDateTime currentDate);
}
