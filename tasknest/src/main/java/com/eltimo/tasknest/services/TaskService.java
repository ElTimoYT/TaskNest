package com.eltimo.tasknest.services;

import com.eltimo.tasknest.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskService {

    Page<?> findAll(Pageable pageable);
    TaskDTO findById(Long id);
    Page<?> findByUserId(Long userId, Pageable pageable);
    TaskDTO save(TaskDTO task);
    Optional<TaskDTO> update(Long id, TaskDTO taskDTO);
    void deleteById(Long id);

}
