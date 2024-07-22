package com.kasunjay.miigrasbackend.repository;

import com.kasunjay.miigrasbackend.entity.web.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    long countEmployeeByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
