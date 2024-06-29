package com.kasunjay.miigrasbackend.repository;

import com.kasunjay.miigrasbackend.entity.web.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {
}
