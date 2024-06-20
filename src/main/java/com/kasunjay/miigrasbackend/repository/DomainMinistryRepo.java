package com.kasunjay.miigrasbackend.repository;

import com.kasunjay.miigrasbackend.entity.web.DomainMinistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainMinistryRepo extends JpaRepository<DomainMinistry, Long> {

}
