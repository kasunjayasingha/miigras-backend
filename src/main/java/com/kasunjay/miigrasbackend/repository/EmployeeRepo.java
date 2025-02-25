package com.kasunjay.miigrasbackend.repository;

import com.kasunjay.miigrasbackend.entity.web.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    long countEmployeeByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    Employee findByUser_Id(Long userId);
}
