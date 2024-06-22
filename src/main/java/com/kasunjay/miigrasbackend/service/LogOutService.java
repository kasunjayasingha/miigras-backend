package com.kasunjay.miigrasbackend.service;

import com.kasunjay.miigrasbackend.entity.Token;
import com.kasunjay.miigrasbackend.repository.TokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogOutService implements LogoutHandler {

    private final TokenRepo tokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader("Access-Token");
        final String jwt;
        if(authHeader != null && !authHeader.startsWith("Bearer ")){
            return;
        }
        jwt = authHeader.substring(7);
        Optional<Token> tokensByAccessTokenEquals = tokenRepository.findTokensByAccessTokenEqualsAndRevokedFalseAndExpiredFalse(jwt);
        tokensByAccessTokenEquals.get().setRevoked(true);
        tokensByAccessTokenEquals.get().setExpired(true);
        tokenRepository.save(tokensByAccessTokenEquals.get());
        SecurityContextHolder.clearContext();
    }
}
