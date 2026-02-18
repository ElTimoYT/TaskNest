package com.eltimo.tasknest.services;

import com.eltimo.tasknest.dto.DashboardDTO;
import com.eltimo.tasknest.entities.User;
import com.eltimo.tasknest.enums.Priority;
import com.eltimo.tasknest.enums.TaskState;
import com.eltimo.tasknest.enums.WorkloadStatus;
import com.eltimo.tasknest.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardService {

    private final TaskRepository taskRepository;
    private final WorkloadService workloadService;

    public DashboardService(TaskRepository taskRepository, WorkloadService workloadService) {
        this.taskRepository = taskRepository;
        this.workloadService = workloadService;
    }

    public DashboardDTO getDashboardStats(User user) {
        LocalDate today = LocalDate.now();

        // 1. Calcular Carga de Trabajo
        WorkloadStatus status = workloadService.calculateWorkload(user);

        // 2. Calcular Progreso Diario
        long totalToday = taskRepository.countByUserIdAndDueDate(user.getId(), today);
        long completedToday = taskRepository.countByUserIdAndDueDateAndState(user.getId(), today, TaskState.DONE);

        int percentage = 0;
        if (totalToday > 0) {
            percentage = (int) ((completedToday * 100) / totalToday);
        }

        // 3. Contadores Globales
        long pending = taskRepository.countByUserIdAndState(user.getId(), TaskState.TODO);
        long urgent = taskRepository.countByUserIdAndPriorityAndState(user.getId(), Priority.HIGH, TaskState.TODO);

        // 4. Mensaje Motivacional según estado
        String message = switch (status) {
            case LIGHT -> "Día tranquilo. ¡Aprovecha para adelantar o descansar!";
            case MODERATE -> "Vas a buen ritmo. Mantén el foco.";
            case HEAVY -> "Día intenso. Prioriza lo urgente y no olvides respirar.";
            case BURNOUT -> "¡PELIGRO! Tienes demasiado encima. Delega o pospón tareas.";
        };

        // 5. Construir respuesta
        return DashboardDTO.builder()
                .workloadStatus(status)
                .motivationMessage(message)
                .totalTasksToday((int) totalToday)
                .completedTasksToday((int) completedToday)
                .dailyProgressPercentage(percentage)
                .totalPending(pending)
                .totalUrgent(urgent)
                .build();
    }
}
