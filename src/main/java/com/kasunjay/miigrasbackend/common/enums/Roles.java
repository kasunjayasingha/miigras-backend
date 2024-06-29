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
   ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_CREATE,
                    ADMIN_DELETE,
                    ADMIN_UPDATE,
                    WORKER_READ,
                    WORKER_CREATE,
                    WORKER_UPDATE,
                    WORKER_DELETE
            )
    ),
    SUPER_ADMIN(
            Set.of(
            ADMIN_READ,
            ADMIN_CREATE,
            ADMIN_DELETE,
            ADMIN_UPDATE,
            WORKER_READ,
            WORKER_CREATE,
            WORKER_UPDATE,
            WORKER_DELETE
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
