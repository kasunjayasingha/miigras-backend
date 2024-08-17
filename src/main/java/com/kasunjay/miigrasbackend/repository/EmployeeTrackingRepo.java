package com.kasunjay.miigrasbackend.repository;

import com.kasunjay.miigrasbackend.entity.mobile.EmployeeTracking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployeeTrackingRepo extends JpaRepository<EmployeeTracking, Long> {
    @Query("SELECT e FROM EmployeeTracking e ORDER BY e.createdDate ASC")
    List<EmployeeTracking> findAllOrderByCreatedDateAsc(Pageable pageable);

    void deleteAllByCreatedDateBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
