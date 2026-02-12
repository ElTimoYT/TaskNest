package com.eltimo.tasknest.services;

import com.eltimo.tasknest.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Page<TaskDTO> findAll(Pageable pageable);
    TaskDTO findById(Long id);
    TaskDTO save(TaskDTO task);
    void deleteById(Long id);

}
