package com.diplom.drinksmachine.domain.staff.admin;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
