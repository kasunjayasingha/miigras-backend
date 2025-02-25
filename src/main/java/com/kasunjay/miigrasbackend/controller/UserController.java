package com.kasunjay.miigrasbackend.controller;

import com.kasunjay.miigrasbackend.common.enums.Success;
import com.kasunjay.miigrasbackend.common.util.AutherizedUserService;
import com.kasunjay.miigrasbackend.entity.User;
import com.kasunjay.miigrasbackend.event.RegistrationCompleteEvent;
import com.kasunjay.miigrasbackend.model.AuthRequestDTO;
import com.kasunjay.miigrasbackend.model.PasswordModel;
import com.kasunjay.miigrasbackend.model.StandardResponse;
import com.kasunjay.miigrasbackend.model.UserModel;
import com.kasunjay.miigrasbackend.service.JWTService;
import com.kasunjay.miigrasbackend.service.core.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ApplicationEventPublisher publisher;

    private final JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<StandardResponse> registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "User created"), HttpStatus.OK);
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<StandardResponse> verifyRegistration(@RequestParam("token") String token) {
        return userService.validateVerificationToken(token);

    }

    @GetMapping("/resendVerifyToken")
    public ResponseEntity<StandardResponse> resendVerificationToken(@RequestParam("token") String oldToken,
                                                                    HttpServletRequest request) {
        return userService.resendVerificationToken(oldToken, applicationUrl(request));
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
        return userService.createPasswordResetTokenForUser(passwordModel,applicationUrl(request));
    }

    @PostMapping("/savePassword")
    public ResponseEntity<StandardResponse> savePassword(@RequestParam("token") String token,
                                                         @RequestBody PasswordModel passwordModel) {
        return userService.saveNewPassword(token, passwordModel);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<StandardResponse> changePassword(@RequestBody PasswordModel passwordModel) {
        return userService.changePassword(passwordModel);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequestDTO){
        return new ResponseEntity<>(
                userService.login(authRequestDTO),HttpStatus.OK
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<StandardResponse> logout() {
        System.out.println("logout called");
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "User logged out"), HttpStatus.OK);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/userStatusChange")
    public ResponseEntity<StandardResponse> userStatusChange(@RequestBody UserModel userModel) {
        return new ResponseEntity<>(userService.userStatusChange(userModel), HttpStatus.OK);
    }

    @PostMapping("/isValidToken")
    public ResponseEntity<StandardResponse> isValidToken(@RequestHeader("Access-Token") String jwtToken){
        return new ResponseEntity<>(userService.isValidToken(jwtToken),HttpStatus.OK);
    }

    @PostMapping("/updateFirebaseStatus")
    public ResponseEntity<StandardResponse> updateFirebaseStatus(@RequestParam("userId") Long userId) {
        return new ResponseEntity<>(userService.updateFirebaseStatus(userId), HttpStatus.OK);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }
}
