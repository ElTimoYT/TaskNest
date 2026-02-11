package com.eltimo.tasknest.services;

import com.eltimo.tasknest.dto.TaskDTO;

import java.util.List;

public interface TaskService {

    List<TaskDTO> findAll();
    TaskDTO findById(Long id);
    TaskDTO save(TaskDTO task);
    void deleteById(Long id);

}
