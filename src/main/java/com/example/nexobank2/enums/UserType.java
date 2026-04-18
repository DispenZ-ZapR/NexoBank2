package com.example.nexobank2.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    CLIENT,
    EMPLOYEE,
    ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
