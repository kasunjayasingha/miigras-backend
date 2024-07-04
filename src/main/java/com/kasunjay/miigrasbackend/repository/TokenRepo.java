package com.kasunjay.miigrasbackend.repository;

import com.kasunjay.miigrasbackend.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<Token,Long> {
    Optional<Token> findTokensByAccessTokenEqualsAndRevokedFalseAndExpiredFalse(String token);

    List<Token> findTokensByUsernameEquals(String username);

    List<Token> findTokensByRevokedTrueAndExpiredTrue();

    Optional<Token> findTokensByAccessToken(String token);
}
