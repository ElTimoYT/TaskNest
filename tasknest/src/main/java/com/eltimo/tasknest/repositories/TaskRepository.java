package com.eltimo.tasknest.repositories;

import com.eltimo.tasknest.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
