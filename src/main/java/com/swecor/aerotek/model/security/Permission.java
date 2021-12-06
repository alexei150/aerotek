package com.swecor.aerotek.model.security;

public enum Permission {
    AEROTEK_SUPER("aerotek:super"),
    AEROTEK_READ("aerotek:read"),
    AEROTEK_WRITE("aerotek:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
