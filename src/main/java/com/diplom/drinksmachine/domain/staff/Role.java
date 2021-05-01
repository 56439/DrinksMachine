package com.diplom.drinksmachine.domain.staff;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, BARISTA;

    @Override
    public String getAuthority() {
        return name();
    }
}
