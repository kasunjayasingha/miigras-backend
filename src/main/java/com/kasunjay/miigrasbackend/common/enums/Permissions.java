package com.kasunjay.miigrasbackend.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permissions {
    WORKER_READ("worker:read"),
    WORKER_UPDATE("worker:update"),
    WORKER_CREATE("worker:create"),
    WORKER_DELETE("worker:delete"),

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    ;

    @Getter
    private final String permission;
}
