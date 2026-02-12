package com.eltimo.tasknest.controllers;

import com.eltimo.tasknest.dto.TaskDTO;
import com.eltimo.tasknest.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public ResponseEntity<Page<TaskDTO>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(taskService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findById(@PathVariable Long id) {
        TaskDTO task = taskService.findById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping()
    public ResponseEntity<TaskDTO> save(@RequestBody TaskDTO taskDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.save(taskDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
