package com.eltimo.tasknest.dto;

import com.eltimo.tasknest.enums.Priority;
import com.eltimo.tasknest.enums.TaskState;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "title", "description", "priority", "state","userId"})
public class TaskDTO {

    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private TaskState taskState;
    private Long userId;

    public TaskDTO() {
    }

    public TaskDTO(Long id, String title, String description, Priority priority, TaskState state, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.taskState = state;
        this.userId = userId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }
}
