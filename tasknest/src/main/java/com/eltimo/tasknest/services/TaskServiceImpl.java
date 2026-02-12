package com.eltimo.tasknest.services;

import com.eltimo.tasknest.dto.TaskDTO;
import com.eltimo.tasknest.entities.Task;
import com.eltimo.tasknest.entities.User;
import com.eltimo.tasknest.repositories.TaskRepository;
import com.eltimo.tasknest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<TaskDTO> findAll(Pageable pageable) {
        return taskRepository
                .findAll(pageable)
                .map(this::convertirADTO);
    }

    @Override
    public TaskDTO findById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        return convertirADTO(task);
    }

    @Override
    public TaskDTO save(TaskDTO taskDTO) {
        Task task;

        if (taskDTO.getId() != null) {
            task = taskRepository.findById(taskDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Tarea no encontrada con id: " + taskDTO.getId()));
        } else {
            task = new Task();
        }

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setPriority(taskDTO.getPriority());
        task.setState(taskDTO.getTaskState());

        User user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + taskDTO.getUserId()));
        task.setUser(user);

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
