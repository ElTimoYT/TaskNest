package com.eltimo.tasknest.services.specifications;

import com.eltimo.tasknest.entities.Task;
import com.eltimo.tasknest.enums.Priority;
import com.eltimo.tasknest.enums.TaskState;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TaskSpecification {

    public static Specification<Task> hasState(TaskState state) {
        return (root, query, criteriaBuilder) -> {
            if (state == null) return null;
            return criteriaBuilder.equal(root.get("state"), state);
        };
    }

    public static Specification<Task> hasPriority(Priority priority) {
        return (root, query, criteriaBuilder) -> {
            if (priority == null) return null;
            return criteriaBuilder.equal(root.get("priority"), priority);
        };
    }

    public static Specification<Task> belongsToUser(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) return null;
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Task> isDueSoon(boolean check) {
        return (root, query, criteriaBuilder) -> {
            if (!check) return null;
            LocalDate today = LocalDate.now();
            LocalDate threeDaysLater = today.plusDays(3); // Definimos "Pronto" como 3 días

            // dueDate >= Hoy AND dueDate <= 3 días
            return criteriaBuilder.between(root.get("dueDate"), today, threeDaysLater);
        };
    }
}
