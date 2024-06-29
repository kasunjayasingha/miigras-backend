package com.kasunjay.miigrasbackend.repository;

import com.kasunjay.miigrasbackend.entity.web.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgencyRepo extends JpaRepository<Agency, Long> {
    List<Agency> findAllByStatus(Boolean status);
}
