export interface Task {
    uuid: string;
    title: string;
    description?: string;
    dueDate: string;
    priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';
    state: 'TODO' | 'IN_PROGRESS' | 'DONE' | 'CANCELED';
    tags: string[];
    notes?: string;
    createdAt?: string;

}

export interface TaskRequest {
    title: string;
    description?: string;
    dueDate: string;
    priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';
    tags: string[];
    notes?: string;
}