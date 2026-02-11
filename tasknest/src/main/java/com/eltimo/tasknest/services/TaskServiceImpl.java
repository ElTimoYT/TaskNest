package com.eltimo.tasknest.services;

import com.eltimo.tasknest.dto.TaskDTO;
import com.eltimo.tasknest.entities.Task;
import com.eltimo.tasknest.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskDTO> findAll() {
        List<Task> task = taskRepository.findAll();
        return task.stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public TaskDTO findById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        return convertirADTO(task);
    }

    @Override
    public TaskDTO save(Task task) {
        Task taskToSave = taskRepository.save(task);
        return convertirADTO(taskToSave);
    }

    @Override
    public void deleteById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        taskRepository.delete(task);
    }

    private TaskDTO convertirADTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getState(),
                task.getUser().getId()
        );
    }
}
