import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' }, // Si entras a la raíz, te lleva al login
    { path: 'login', component: LoginComponent },
    //{ path: 'register', component: RegisterComponent }, // (Comentado hasta que lo creemos)
   { path: 'dashboard', component: DashboardComponent } // (Comentado hasta que lo creemos)
];
