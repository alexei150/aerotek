package com.swecor.aerotek.model.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(Set.of(Permission.AEROTEK_SUPER ,Permission.AEROTEK_READ, Permission.AEROTEK_WRITE)),
    EDITOR(Set.of(Permission.AEROTEK_READ, Permission.AEROTEK_WRITE)),
    USER(Set.of(Permission.AEROTEK_READ)),
    TECHNICAL_DIR(Set.of(Permission.AEROTEK_READ, Permission.AEROTEK_WRITE)),
    COMMERCIAL_DIR(Set.of(Permission.AEROTEK_READ, Permission.AEROTEK_WRITE)),
    MANAGER(Set.of(Permission.AEROTEK_READ, Permission.AEROTEK_WRITE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
