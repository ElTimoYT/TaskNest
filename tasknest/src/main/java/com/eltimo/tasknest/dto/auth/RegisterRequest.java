package com.eltimo.tasknest.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    @Size(min=4)
    private String username;

    @Email
    @NotEmpty
    private String email;

    @NotBlank
    @Size(min=8)
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
