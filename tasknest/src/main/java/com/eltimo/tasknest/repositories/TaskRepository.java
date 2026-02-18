package com.eltimo.tasknest.repositories;

import com.eltimo.tasknest.entities.Task;
import com.eltimo.tasknest.enums.Priority;
import com.eltimo.tasknest.enums.TaskState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {
    Page<Task> findByUserId(Long userId, Pageable pageable);
    Optional<Task> findByUuid(String uuid);

    // 1. Para la barra de progreso de HOY
    long countByUserIdAndDueDate(Long userId, LocalDate dueDate);
    long countByUserIdAndDueDateAndState(Long userId, LocalDate dueDate, TaskState state);

    // 2. Para los contadores globales
    long countByUserIdAndState(Long userId, TaskState state);
    long countByUserIdAndPriorityAndState(Long userId, Priority priority, TaskState state);
}
