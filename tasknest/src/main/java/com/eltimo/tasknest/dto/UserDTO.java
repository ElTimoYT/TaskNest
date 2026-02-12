package com.eltimo.tasknest.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({"id", "name", "username", "email"})
public class UserDTO {


    private Long id;
    private String name;
    private String username;
    private String email;
    private List<TaskDTO> tasks;

    public UserDTO() {
    }

    public UserDTO(Long id, String name, String username, String email, List<TaskDTO> tasks) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.tasks = tasks;
    }

}
