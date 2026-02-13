package com.eltimo.tasknest.controllers;

import com.eltimo.tasknest.dto.TaskDTO;
import com.eltimo.tasknest.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

import static com.eltimo.tasknest.utils.ValidationUtils.validation;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<?> findAll(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(taskService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<TaskDTO> taskDTO = Optional.ofNullable(taskService.findById(id));
        if(taskDTO.isPresent()) {
            return ResponseEntity.ok(taskDTO.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "The task was not found by id: " + id));
    }

    @PostMapping()
    public ResponseEntity<?> save(@Valid @RequestBody TaskDTO taskDTO, BindingResult result) {
        if(result.hasErrors()) {
            if(result.getFieldError() != null ){
                return validation(result);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", result.getFieldError().getDefaultMessage()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.save(taskDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
