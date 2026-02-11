package com.eltimo.tasknest.dto;

import com.eltimo.tasknest.entities.Task;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }
}
