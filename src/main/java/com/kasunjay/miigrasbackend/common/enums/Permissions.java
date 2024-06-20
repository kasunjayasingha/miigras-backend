package com.kasunjay.miigrasbackend.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permissions {
    WORKER_READ("cashier:read"),
    WORKER_UPDATE("cashier:update"),
    WORKER_CREATE("cashier:create"),
    WORKER_DELETE("cashier:delete"),
    STOCK_MANAGER_READ("stockmanager:read"),
    STOCK_MANAGER_UPDATE("stockmanager:update"),
    STOCK_MANAGER_CREATE("stockmanager:create"),
    STOCK_MANAGER_DELETE("stockmanager:delete"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    ;

    @Getter
    private final String permission;
}
