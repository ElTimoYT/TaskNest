export interface DashboardData {
    workloadStatus: 'LIGHT' | 'MODERATE' | 'HEAVY' | 'BURNOUT';
    motivationMessage: string;
    totalTasksToday: number;
    completedTasksToday: number;
    dailyProgressPercentage: number;
    totalPending: number;
    totalUrgent: number;
}