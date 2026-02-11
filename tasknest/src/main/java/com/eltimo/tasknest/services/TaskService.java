package com.eltimo.tasknest.services;

import com.eltimo.tasknest.dto.TaskDTO;
import com.eltimo.tasknest.entities.Task;

import java.util.List;

public interface TaskService {

    List<TaskDTO> findAll();
    TaskDTO findById(Long id);
    TaskDTO save(Task task);
    void deleteById(Long id);

}
