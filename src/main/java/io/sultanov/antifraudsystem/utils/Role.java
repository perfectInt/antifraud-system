package io.sultanov.antifraudsystem.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    ADMINISTRATOR("ADMINISTRATOR"),
    MERCHANT("MERCHANT"),
    SUPPORT("SUPPORT");

    private final String role;

    @Override
    public String getAuthority() {
        return role;
    }
}
