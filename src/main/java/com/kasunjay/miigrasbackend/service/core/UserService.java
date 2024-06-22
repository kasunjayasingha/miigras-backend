package com.kasunjay.miigrasbackend.service.core;

import com.kasunjay.miigrasbackend.entity.User;
import com.kasunjay.miigrasbackend.model.*;
import org.springframework.http.ResponseEntity;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    ResponseEntity<StandardResponse> validateVerificationToken(String token);

    ResponseEntity<StandardResponse> resendVerificationToken(String oldToken, String s);

    String createPasswordResetTokenForUser(PasswordModel passwordModel, String s);

    ResponseEntity<StandardResponse> saveNewPassword(String token, PasswordModel passwordModel);

    ResponseEntity<StandardResponse> changePassword(PasswordModel passwordModel);

    AuthResponseDTO login(AuthRequestDTO authRequestDTO);

    void removeExpiredTokens();
}
