package com.kasunjay.miigrasbackend.repository;


import com.kasunjay.miigrasbackend.common.enums.Roles;
import com.kasunjay.miigrasbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Boolean existsByEmail(String email);

    List<User> findAllByRoleIsNot(Roles role);
}
