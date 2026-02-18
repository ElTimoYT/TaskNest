package com.eltimo.tasknest.dto;

import com.eltimo.tasknest.enums.WorkloadStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DashboardDTO {

    private WorkloadStatus workloadStatus;
    private String motivationMessage;

    private int totalTasksToday;
    private int completedTasksToday;
    private int dailyProgressPercentage;

    private long totalPending;
    private long totalUrgent;

}
