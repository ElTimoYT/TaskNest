package com.eltimo.tasknest.entities;

import com.eltimo.tasknest.enums.TaskState;
import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskState state;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }
}
