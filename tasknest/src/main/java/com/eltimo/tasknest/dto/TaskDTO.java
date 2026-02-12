package com.eltimo.tasknest.dto;

import com.eltimo.tasknest.enums.Priority;
import com.eltimo.tasknest.enums.TaskState;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
