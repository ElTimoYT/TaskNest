package com.eltimo.tasknest.controllers;

import com.eltimo.tasknest.dto.TaskDTO;
import com.eltimo.tasknest.entities.User;
import com.eltimo.tasknest.enums.Priority;
import com.eltimo.tasknest.enums.TaskState;
import com.eltimo.tasknest.services.TaskService;
import com.eltimo.tasknest.services.WorkloadService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

import static com.eltimo.tasknest.utils.ValidationUtils.validation;

@RestController
@RequestMapping("/api/tasks")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;
    private final WorkloadService workloadService;

    @Autowired
    public TaskController(TaskService taskService, WorkloadService workloadService) {
        this.taskService = taskService;
        this.workloadService = workloadService;
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<?> findAll(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(taskService.findAll(pageable));
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<?> findMyTasks(
            @RequestParam(required = false) TaskState state,     // ?state=PENDING
            @RequestParam(required = false) Priority priority,   // ?priority=HIGH
            @RequestParam(required = false, defaultValue = "false") boolean dueSoon, // ?dueSoon=true
            @PageableDefault(size = 10, page = 0, sort = "dueDate") Pageable pageable,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                taskService.findMyTasks(user.getId(), state, priority, dueSoon, pageable)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable Long userId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(taskService.findByUserId(userId, pageable));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> findByUuid(
            @PathVariable String uuid,
            @AuthenticationPrincipal User user
    ) {
        TaskDTO task = taskService.findByUuid(uuid);

        if (!task.getUserId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to view this task");
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping()
    public ResponseEntity<?> save(@Valid @RequestBody TaskDTO taskDTO, BindingResult result, @AuthenticationPrincipal User user) {
        if(result.hasErrors()) {
            if(result.getFieldError() != null ){
                return validation(result);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", result.getFieldError().getDefaultMessage()));
        }

        if (taskDTO.getUuid() != null) {
            TaskDTO existingTask = taskService.findByUuid(taskDTO.getUuid());
            if (existingTask != null && !existingTask.getUserId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No puedes editar tareas de otros.");
            }
        }

        taskDTO.setUserId(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.save(taskDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id, @AuthenticationPrincipal User user) {

        TaskDTO taskDTO = taskService.findById(id);
        if (taskDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "The task was not found by id: " + id));
        }
        if (!taskDTO.getUserId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this task");
        }

        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{uuid}") // Recibe UUID string
    public ResponseEntity<?> deleteByUuid(
            @PathVariable String uuid,
            @AuthenticationPrincipal User user
    ) {
        TaskDTO task = taskService.findByUuid(uuid); // Buscamos primero para validar

        if (!task.getUserId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No puedes borrar esto");
        }

        taskService.deleteByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/workload")
    public ResponseEntity<?> getWorkload(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(workloadService.calculateWorkload(user));
    }

}
