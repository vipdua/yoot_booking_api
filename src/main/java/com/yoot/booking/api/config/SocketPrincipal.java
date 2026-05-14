package com.yoot.booking.api.config;

import java.security.Principal;

public class SocketPrincipal implements Principal {

    private final String email;

    public SocketPrincipal(String email) {
        this.email = email;
    }

    @Override
    public String getName() {
        return email;
    }
}
