package com.kasunjay.miigrasbackend.event.listener;

import com.kasunjay.miigrasbackend.entity.User;
import com.kasunjay.miigrasbackend.event.RegistrationCompleteEvent;
import com.kasunjay.miigrasbackend.service.core.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
//Create the Verification Token for the User with Link
       User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token,user);
        //Send Mail to user
        String url =
                event.getApplicationUrl()
                        + "/api/v1/user/verifyRegistration?token="
                        +token;

        //sendVerificationEmail()
        log.info("Click the link to verify your account: {}",
                url);

    }
}
