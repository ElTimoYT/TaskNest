package com.eltimo.tasknest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest implements IUser {

    @NotBlank
    private String name;

    @Email
    @NotEmpty
    private String email;

    @NotBlank
    @Size(min = 4, max=12)
    private String username;

    private boolean admin;

    @Override
    public boolean isAdmin() {
        return admin;
    }

}
