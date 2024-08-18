package com.kasunjay.miigrasbackend.repository;

import com.kasunjay.miigrasbackend.entity.mobile.EmployeeTracking;
import com.kasunjay.miigrasbackend.entity.web.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeTrackingRepo extends JpaRepository<EmployeeTracking, Long> {
    @Query("SELECT e FROM EmployeeTracking e WHERE e.isAvailable = :isAvailable ORDER BY e.createdDate ASC")
    List<EmployeeTracking> findAllOrderByCreatedDateAsc( Pageable pageable,@Param("isAvailable") boolean isAvailable);

    void deleteAllByCreatedDateBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);

    Optional<EmployeeTracking> findFirstByEmployeeOrderByCreatedDateDesc(Employee employee);

    Optional<EmployeeTracking> findByEmployeeAndIsAvailable(Employee employee, Boolean isAvailable);

    @Query(value = "SELECT * FROM employee_realtime_location " +
            "WHERE ST_Distance_Sphere(point(:longitude, :latitude), point(longitude, latitude)) <= :radius " +
            "AND is_available = :isAvailable",
            nativeQuery = true)
    List<EmployeeTracking> findEmployeesWithinRadius(@Param("latitude") double latitude,
                                                     @Param("longitude") double longitude,
                                                     @Param("radius") double radius,
                                                     @Param("isAvailable") boolean isAvailable);
}
