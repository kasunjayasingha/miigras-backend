package com.kasunjay.miigrasbackend.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.kasunjay.miigrasbackend.common.enums.Permissions.*;

@RequiredArgsConstructor
public enum Roles {
    WORKER(
            Set.of(
                    WORKER_READ,
                    WORKER_CREATE,
                    WORKER_UPDATE,
                    WORKER_DELETE
            )
    ),
    STOCK_MANAGER(
            Set.of(
                    STOCK_MANAGER_CREATE,
                    STOCK_MANAGER_DELETE,
                    STOCK_MANAGER_READ,
                    STOCK_MANAGER_UPDATE
            )
    ), ADMIN(
            Set.of(
                    STOCK_MANAGER_CREATE,
                    STOCK_MANAGER_DELETE,
                    STOCK_MANAGER_READ,
                    STOCK_MANAGER_UPDATE,
                    ADMIN_READ,
                    ADMIN_CREATE,
                    ADMIN_DELETE,
                    ADMIN_UPDATE
            )
    );
    @Getter
    private final Set<Permissions> permissions;
}
