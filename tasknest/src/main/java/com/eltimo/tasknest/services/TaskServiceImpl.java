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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    @Transactional(readOnly = true)
    public Page<?> findAll(Pageable pageable) {
        return taskRepository
                .findAll(pageable)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO findById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        return convertirADTO(task);
    }

    @Override
    public Page<?> findByUserId(Long userId, Pageable pageable) {
        return taskRepository
                .findByUserId(userId, pageable)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional
    public TaskDTO save(TaskDTO taskDTO) {
        User user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + taskDTO.getUserId()));

        Task taskToSave = new Task();
        taskToSave.setTitle(taskDTO.getTitle());
        taskToSave.setDescription(taskDTO.getDescription());
        taskToSave.setPriority(taskDTO.getPriority());
        taskToSave.setState(taskDTO.getTaskState());
        taskToSave.setUser(user);

        Task savedTask = taskRepository.save(taskToSave);
        return convertirADTO(savedTask);
    }

    @Override
    @Transactional
    public Optional<TaskDTO> update(Long id, TaskDTO taskDTO) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task taskToUpdate = taskOptional.get();
            taskToUpdate.setTitle(taskDTO.getTitle());
            taskToUpdate.setDescription(taskDTO.getDescription());
            taskToUpdate.setPriority(taskDTO.getPriority());
            taskToUpdate.setState(taskDTO.getTaskState());

            User user = userRepository.findById(taskDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + taskDTO.getUserId()));
            taskToUpdate.setUser(user);

            Task updatedTask = taskRepository.save(taskToUpdate);
            return Optional.of(convertirADTO(updatedTask));
        } else {
            return Optional.empty();
        }
    }


    @Override
    @Transactional
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
