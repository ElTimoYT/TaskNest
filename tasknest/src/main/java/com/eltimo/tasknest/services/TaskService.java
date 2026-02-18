package com.eltimo.tasknest.services;

import com.eltimo.tasknest.dto.TaskDTO;
import com.eltimo.tasknest.enums.Priority;
import com.eltimo.tasknest.enums.TaskState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskService {

    Page<?> findAll(Pageable pageable);
    TaskDTO findById(Long id);
    Page<?> findByUserId(Long userId, Pageable pageable);
    Page<?> findMyTasks(Long userId, TaskState state, Priority priority, boolean dueSoon, Pageable pageable);
    TaskDTO findByUuid(String uuid);
    void deleteByUuid(String uuid);
    TaskDTO save(TaskDTO task);
    Optional<TaskDTO> update(Long id, TaskDTO taskDTO);
    void deleteById(Long id);

}
