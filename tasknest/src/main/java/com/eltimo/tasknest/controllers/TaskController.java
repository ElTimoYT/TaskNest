package com.eltimo.tasknest.controllers;

import com.eltimo.tasknest.dto.TaskDTO;
import com.eltimo.tasknest.entities.Task;
import com.eltimo.tasknest.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public List<TaskDTO> findAll() {
        return taskService.findAll();
    }

    @GetMapping("/tasks/{id}")
    public TaskDTO findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping("/tasks")
    public TaskDTO save(@RequestBody TaskDTO taskDTO) {
        return taskService.save(taskDTO);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteById(@PathVariable Long id) {
        taskService.deleteById(id);
    }

}
