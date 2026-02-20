import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { AuthResponse, LoginRequest, RegisterRequest } from "../models/auth.models";

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:8080/api/auth';

    login(credentials: LoginRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials);
    }

    register(data: RegisterRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/register`, data);
    }

    changePassword(currentPassword: string, newPassword: string): Observable<any> {
    // Asegúrate de que la URL coincide con donde pusiste el Endpoint en Spring Boot
    // Por ejemplo: si lo pusiste en AuthController que tiene @RequestMapping("/api/auth")
        return this.http.put(`http://localhost:8080/api/users/change-password`, {
            currentPassword,
            newPassword
        });
    }

}