package com.eltimo.tasknest.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
    private String email;
    private String password;

    public AuthenticationRequest() {
    }
    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }


}
