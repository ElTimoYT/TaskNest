import { Injectable, signal } from '@angular/core';

export interface Toast {
  id: string;
  title: string;
  message: string;
  type: 'success' | 'error' | 'info';
  duration: number;
  leaving?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  // Lista reactiva de notificaciones activas
  toasts = signal<Toast[]>([]);

  show(title: string, message: string, type: 'success' | 'error' | 'info' = 'info', duration = 4000) {
    const id = Math.random().toString(36).substring(2, 9);
    
    // Al crearse, 'leaving' es falso
    this.toasts.update(currentToasts => [...currentToasts, { id, title, message, type, duration, leaving: false }]);

    setTimeout(() => this.remove(id), duration);
  }

  remove(id: string) {
    // 1. Primero, le decimos al toast que active su animación de salida (Fade Out)
    this.toasts.update(currentToasts => 
      currentToasts.map(t => t.id === id ? { ...t, leaving: true } : t)
    );

    // 2. Esperamos 300ms (lo que dura la animación CSS) para borrarlo del HTML de verdad
    setTimeout(() => {
      this.toasts.update(currentToasts => currentToasts.filter(t => t.id !== id));
    }, 300);
  }
}