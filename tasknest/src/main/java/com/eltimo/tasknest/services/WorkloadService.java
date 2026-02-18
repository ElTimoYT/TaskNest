package com.eltimo.tasknest.services;

import com.eltimo.tasknest.entities.Task;
import com.eltimo.tasknest.entities.User;
import com.eltimo.tasknest.enums.TaskState;
import com.eltimo.tasknest.enums.WorkloadStatus;
import com.eltimo.tasknest.repositories.TaskRepository;
import com.eltimo.tasknest.services.specifications.TaskSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkloadService {

    private  final TaskRepository taskRepository;

    public WorkloadService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public WorkloadStatus calculateWorkload(User user){
        Specification<Task> specification = Specification.where(TaskSpecification.belongsToUser(user.getId()))
                .and(TaskSpecification.hasState(TaskState.TODO))
                .and(TaskSpecification.hasState(TaskState.IN_PROGRESS));

        List<Task> tasks = taskRepository.findAll(specification);

        int score = 0;

        for (Task task : tasks) {
            switch (task.getPriority()) {
                case LOW -> score += 1;
                case MEDIUM -> score += 2;
                case HIGH -> score += 3;
                case URGENT -> score += 4;
            }
        }

        if (score <= 5) return WorkloadStatus.LIGHT;
        else if (score <= 10) return WorkloadStatus.MODERATE;
        else return WorkloadStatus.HEAVY;

    }
}
