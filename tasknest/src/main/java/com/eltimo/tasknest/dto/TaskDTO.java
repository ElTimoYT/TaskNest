package com.eltimo.tasknest.dto;

import com.eltimo.tasknest.enums.Priority;
import com.eltimo.tasknest.enums.TaskState;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@JsonPropertyOrder({"id", "title", "description", "priority", "state","userId"})
public class TaskDTO {

    private String uuid;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, message = "El título debe tener al menos 3 caracteres")
    private String title;
    private String description;
    private String notes;
    private Priority priority;
    private TaskState taskState;
    private Long userId;
    private Set<String> tags;

    @FutureOrPresent(message = "Due date must be in the present or future")
    private LocalDate dueDate;

    public TaskDTO() {
    }

    public TaskDTO(String uuid, String title, String description, String notes, Priority priority, TaskState state, Long userId, LocalDate dueDate, Set<String> tags) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.notes = notes;
        this.priority = priority;
        this.taskState = state;
        this.userId = userId;
        this.dueDate = dueDate;
        this.tags = tags;
    }


}
