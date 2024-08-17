package com.kasunjay.miigrasbackend.controller;

import com.kasunjay.miigrasbackend.service.core.MobileService;
import com.kasunjay.miigrasbackend.service.core.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
public class ScheduledController {
    private final UserService userService;
    private final MobileService mobileService;

    private LocalDateTime lastExecutionDate = LocalDateTime.now().minusMonths(2);

    @Scheduled(cron = "0 */10 * * * ?")
//    @Scheduled(cron = "*/5 * * * * ?")
    public void cronJobSch() throws Exception {
        userService.removeExpiredTokens();
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void dailyScheduledTask() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        if (ChronoUnit.MONTHS.between(lastExecutionDate, now) >= 2) {
            mobileService.removeEveryLocationData();
            lastExecutionDate = now; // Update the last execution date
        }
    }
}
