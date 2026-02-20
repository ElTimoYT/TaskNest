import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { map, Observable } from "rxjs";
import { Task, TaskRequest } from "../models/task.model";

@Injectable({
  providedIn: 'root'
})
export class TaskService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:8080/api/tasks';

    getTasks(state?: string, priority?: string): Observable<Task[]> {
        let params = new HttpParams();
        if (state) params = params.set('state', state);
        if (priority) params = params.set('priority', priority);

        
        return this.http.get<any>(`${this.apiUrl}/my-tasks`, { params }).pipe(
            map(response => response.content || [])
        );
    }

    getTaskById(uuid: string): Observable<Task> {
        return this.http.get<Task>(`${this.apiUrl}/${uuid}`);
    }

    createTask(task: TaskRequest): Observable<Task> {
        return this.http.post<Task>(this.apiUrl, task);
    }

    changeTaskState(uuid: string, state: string): Observable<Task> {
        return this.http.patch<Task>(`${this.apiUrl}/${uuid}/state`, { state });
    }

    updateTask(uuid: string, task: TaskRequest): Observable<Task> {
        const payload = { ...task, uuid: uuid };
        return this.http.post<Task>(`${this.apiUrl}`, payload);
    }

    deleteTask(uuid: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${uuid}`);
  }
}