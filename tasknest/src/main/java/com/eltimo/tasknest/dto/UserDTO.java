package com.eltimo.tasknest.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({"id", "name", "username", "email"})
public class UserDTO {


    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String username;

    @Email
    @NotBlank
    private String email;
    private List<TaskDTO> tasks;
    private boolean isNotificationEnabled;

    public UserDTO() {
    }

    public UserDTO(Long id, String name, String username, String email, List<TaskDTO> tasks, boolean isNotificationEnabled) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.tasks = tasks;
        this.isNotificationEnabled = isNotificationEnabled;
    }

}
