package com.kasunjay.miigrasbackend.service.core.impl;

import com.kasunjay.miigrasbackend.common.enums.Roles;
import com.kasunjay.miigrasbackend.common.enums.Success;
import com.kasunjay.miigrasbackend.common.exception.UserException;
import com.kasunjay.miigrasbackend.common.mapper.UserMapper;
import com.kasunjay.miigrasbackend.common.util.AutherizedUserService;
import com.kasunjay.miigrasbackend.entity.PasswordResetToken;
import com.kasunjay.miigrasbackend.entity.Token;
import com.kasunjay.miigrasbackend.entity.User;
import com.kasunjay.miigrasbackend.entity.VerificationToken;
import com.kasunjay.miigrasbackend.model.*;
import com.kasunjay.miigrasbackend.repository.PasswordResetTokenRepo;
import com.kasunjay.miigrasbackend.repository.TokenRepo;
import com.kasunjay.miigrasbackend.repository.UserRepo;
import com.kasunjay.miigrasbackend.repository.VerificationTokenRepo;
import com.kasunjay.miigrasbackend.service.JWTService;
import com.kasunjay.miigrasbackend.service.core.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final VerificationTokenRepo verificationTokenRepository;
    private final UserRepo userRepository;
    private final UserMapper userMapper;
    private final PasswordResetTokenRepo passwordResetTokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final TokenRepo tokenRepository;
    private final UserDetailsService userDetailsService;

    @Override
    public User registerUser(UserModel userModel) {
        try {
            log.info("registerUser service method called");
            User user = userMapper.userModelToUser(userModel);
            user.setRole(userModel.getRole());
            user.setPassword(passwordEncoder.encode(userModel.getPassword()));
            return userRepository.save(user);
        }catch (Exception e) {
            e.printStackTrace();
            throw new UserException("User registration failed: " + e.getMessage());
        }
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {

        VerificationToken verificationToken = new VerificationToken(user, token);

        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public ResponseEntity<StandardResponse> validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return new ResponseEntity<>(new StandardResponse(HttpStatus.BAD_REQUEST, Success.FAILURE, "Invalid Token"), HttpStatus.BAD_REQUEST);
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
//            verificationTokenRepository.delete(verificationToken);
            return new ResponseEntity<>(new StandardResponse(HttpStatus.BAD_REQUEST, Success.FAILURE, "Token Expired"), HttpStatus.BAD_REQUEST);
        }

        user.setEnabled(true);
        userRepository.save(user);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "User Verified Successfully"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StandardResponse> resendVerificationToken(String oldToken, String s) {
        VerificationToken verificationToken
                = verificationTokenRepository.findByToken(oldToken);
        if (verificationToken == null) {
            return new ResponseEntity<>(new StandardResponse(HttpStatus.BAD_REQUEST, Success.FAILURE, "Invalid Token"), HttpStatus.BAD_REQUEST);
        }
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken = verificationTokenRepository.save(verificationToken);

        User user = verificationToken.getUser();

        String url = s + "/api/v1/user/verifyRegistration?token=" + verificationToken.getToken();
        log.info("Click the link to verify your account: " + url);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Verification Link Sent"), HttpStatus.OK);
    }

    @Override
    public String createPasswordResetTokenForUser(PasswordModel passwordModel, String s) {
        User user = userRepository.findByEmail(passwordModel.getEmail());
        if (user == null) {
            throw new UserException("User not found");
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepo.save(passwordResetToken);
        return passwordResetTokenMail(user, s, token);
    }

    @Override
    public ResponseEntity<StandardResponse> saveNewPassword(String token, PasswordModel passwordModel) {
        try {
            User user = validatePasswordResetToken(token);
            if( user== null){
                throw new UserException("Invalid Token");
            }
            user.setPassword(passwordEncoder.encode(passwordModel.getNewPassword()));
            userRepository.save(user);
            return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Password Reset Successfully"), HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            throw new UserException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<StandardResponse> changePassword(PasswordModel passwordModel) {
        try {
            Optional<User> user = Optional.ofNullable(userRepository.findByEmail(passwordModel.getEmail()));
            if (!user.isPresent()) {
                throw new UserException("User not found");
            }
            if (!passwordEncoder.matches(passwordModel.getOldPassword(), user.get().getPassword())) {
                throw new UserException("Invalid Old Password");
            }
            if (passwordModel.getNewPassword() == null || passwordModel.getNewPassword().length() < 3) {
                throw new UserException("Password must be at least 3 characters long");
            }
            user.get().setPassword(passwordModel.getNewPassword());
            userRepository.save(user.get());
            return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Password Changed Successfully"), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            throw new UserException(e.getMessage());
        }
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) {
        try{
            Optional<User> user = Optional.ofNullable(userRepository.findByEmail(authRequestDTO.getUsername()));
            if (!user.isPresent()) {
                throw new UserException("User not found");
            }
            if (!user.get().isEnabled()) {
                throw new UserException("User not verified");
            }

            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword())
            );
            if (authenticate.isAuthenticated()) {
                revokeAllUserTokens(user.get().getEmail());
                String access_token = jwtService.generateToken(userDetailsService.loadUserByUsername(user.get().getEmail()));
                Token token = new Token(access_token,user.get().getEmail());
                tokenRepository.save(token);
                return new AuthResponseDTO(
                        user.get().getId(),
                        user.get().getRole(),
                        access_token
                );
            } else {
                throw new UserException("Invalid Credentials");
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new UserException(e.getMessage());
        }
    }

    public User validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passwordResetToken
                = passwordResetTokenRepo.findByToken(token);

        if (!passwordResetToken.isPresent()) {
            throw new UserException("Invalid Token");
        }

        User user = passwordResetToken.get().getUser();
        Calendar cal = Calendar.getInstance();

        if ((passwordResetToken.get().getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            passwordResetTokenRepo.delete(passwordResetToken.get());
            throw new UserException("Token Expired");
        }
        return user;
    }

    public void removeExpiredTokens() {
        tokenRepository.findTokensByRevokedTrueAndExpiredTrue()
                .forEach(token -> {
                    tokenRepository.delete(token);
                });
    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url =
                applicationUrl
                        + "/api/v1/user/savePassword?token="
                        + token;
        log.info("Click the link to Reset your Password: {}",
                url);
        return url;
    }

    private void revokeAllUserTokens(String username){
        tokenRepository.findTokensByUsernameEquals(username)
                .forEach(token -> {
                    token.setRevoked(true);
                    token.setExpired(true);
                    tokenRepository.save(token);
                });
    }
}
