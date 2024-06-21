package com.kasunjay.miigrasbackend.common.util;

import com.kasunjay.miigrasbackend.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AutherizedUserService {
    public static User getAutherizedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
