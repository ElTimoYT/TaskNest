import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { DashboardData } from "../models/dashboard.model";

@Injectable({
    providedIn: 'root'
})
export class DashboardService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:8080/api/dashboard';

    getDashboardStats(): Observable<DashboardData> {
        return this.http.get<DashboardData>(`${this.apiUrl}`);
    }
}