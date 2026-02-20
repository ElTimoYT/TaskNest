import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatMenuModule } from '@angular/material/menu'; // 👈 NUEVO IMPORT

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, MatIconModule, MatMenuModule, MatDividerModule], // 👈 AÑÁDELO AQUÍ
  templateUrl: './main-layout.component.html'
})
export class MainLayoutComponent implements OnInit{
  private router = inject(Router);

  userInitials: string = 'US';
  usernameDisplay: string = '';
  isDarkMode = signal<boolean>(false);

  ngOnInit() {
    this.extractUserInitials();
    this.checkTheme();
    this.extractUsername();
  }

  checkTheme() {
    const savedTheme = localStorage.getItem('theme');
    // Si el usuario guardó "dark" o si su Windows/Mac está en modo oscuro por defecto
    if (savedTheme === 'dark' || (!savedTheme && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
      this.isDarkMode.set(true);
      document.documentElement.classList.add('dark'); // Le pone la clase 'dark' al <html>
    } else {
      this.isDarkMode.set(false);
      document.documentElement.classList.remove('dark');
    }
  }

  toggleTheme() {
    if (this.isDarkMode()) {
      // Pasar a modo claro
      document.documentElement.classList.remove('dark');
      localStorage.setItem('theme', 'light');
      this.isDarkMode.set(false);
    } else {
      // Pasar a modo oscuro
      document.documentElement.classList.add('dark');
      localStorage.setItem('theme', 'dark');
      this.isDarkMode.set(true);
    }
  }

  extractUserInitials() {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        // Descodificamos la carga útil (payload) del JWT de forma segura
        const payload = JSON.parse(atob(token.split('.')[1]));
        const username = payload.sub || payload.email || 'User';
        
        // Cogemos las dos primeras letras y las ponemos en mayúsculas (ej: admin@... -> AD)
        this.userInitials = username.substring(0, 2).toUpperCase();
      } catch (e) {
        console.error('Error leyendo el token', e);
      }
    }
  }

  extractUsername() {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        // Descodificamos la carga útil (payload) del JWT de forma segura
        const payload = JSON.parse(atob(token.split('.')[1]));
        const username = payload.sub || payload.email || 'User';
        this.usernameDisplay = username;
      } catch (e) {
        console.error('Error leyendo el token', e);
      }
    }
  }

  logout() {
    // 1. Borramos el token para que el Guard nos bloquee la próxima vez
    localStorage.removeItem('token'); 
    
    // Opcional: Si guardas otras cosas como el usuario, bórralas también
    // localStorage.removeItem('user'); 
    
    // 2. Redirigimos al Login
    this.router.navigate(['/login']);
  }
}
