package com.kasunjay.miigrasbackend.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
