import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  template: `
    <div class="fixed top-24 md:top-6 right-6 z-[99999] flex flex-col gap-4 pointer-events-none">
      
      <div *ngFor="let toast of toastService.toasts()" 
           class="pointer-events-auto w-80 md:w-96 bg-white/95 dark:bg-slate-800/95 backdrop-blur-xl border border-slate-200 dark:border-slate-700/80 shadow-2xl shadow-indigo-500/10 rounded-2xl overflow-hidden flex flex-col"
           [ngClass]="toast.leaving ? 'animate-fade-out' : 'animate-slide-in-right'">
           <div class="p-4 flex items-start gap-4">
          <div class="shrink-0 mt-0.5" [ngSwitch]="toast.type">
            <div *ngSwitchCase="'success'" class="h-9 w-9 rounded-full bg-emerald-100 dark:bg-emerald-500/20 flex items-center justify-center text-emerald-600 dark:text-emerald-400 shadow-inner">
              <mat-icon class="!h-5 !w-5 !text-[20px]">check_circle</mat-icon>
            </div>
            <div *ngSwitchCase="'error'" class="h-9 w-9 rounded-full bg-rose-100 dark:bg-rose-500/20 flex items-center justify-center text-rose-600 dark:text-rose-400 shadow-inner">
              <mat-icon class="!h-5 !w-5 !text-[20px]">error_outline</mat-icon>
            </div>
            <div *ngSwitchDefault class="h-9 w-9 rounded-full bg-indigo-100 dark:bg-indigo-500/20 flex items-center justify-center text-indigo-600 dark:text-indigo-400 shadow-inner">
              <mat-icon class="!h-5 !w-5 !text-[20px]">info</mat-icon>
            </div>
          </div>

          <div class="flex-1 pt-0.5">
            <h4 class="text-sm font-extrabold text-slate-800 dark:text-white tracking-tight">{{ toast.title }}</h4>
            <p class="text-[13px] text-slate-500 dark:text-slate-400 mt-1 leading-relaxed">{{ toast.message }}</p>
          </div>

          <button (click)="toastService.remove(toast.id)" 
                  class="shrink-0 h-8 w-8 flex items-center justify-center rounded-full text-slate-400 hover:text-slate-700 hover:bg-slate-100 dark:hover:text-white dark:hover:bg-slate-700 transition-all cursor-pointer outline-none">
            <mat-icon class="!h-4 !w-4 !text-[16px]">close</mat-icon>
          </button>
        </div>

        <div class="h-1 w-full bg-slate-100 dark:bg-slate-700/50">
          <div class="h-full bg-gradient-to-r progress-shrink-animation"
               [ngClass]="{
                 'from-emerald-400 to-emerald-500': toast.type === 'success',
                 'from-rose-400 to-rose-500': toast.type === 'error',
                 'from-indigo-400 to-purple-500': toast.type === 'info'
               }"
               [style.animation-duration.ms]="toast.duration">
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    /* Animación de Entrada */
    @keyframes slideInRight {
      0% { transform: translateX(120%); opacity: 0; }
      70% { transform: translateX(-10px); opacity: 1; }
      100% { transform: translateX(0); opacity: 1; }
    }
    .animate-slide-in-right {
      animation: slideInRight 0.5s cubic-bezier(0.25, 1, 0.5, 1) forwards;
    }

    /* 🌟 NUEVA Animación de Salida (Fade y deslizamiento hacia la derecha) */
    @keyframes fadeOutRight {
      0% { transform: translateX(0) scale(1); opacity: 1; }
      100% { transform: translateX(30px) scale(0.95); opacity: 0; }
    }
    .animate-fade-out {
      animation: fadeOutRight 0.3s cubic-bezier(0.4, 0, 0.2, 1) forwards;
    }

    @keyframes shrinkWidth {
      0% { width: 100%; }
      100% { width: 0%; }
    }
    .progress-shrink-animation {
      animation-name: shrinkWidth;
      animation-timing-function: linear;
      animation-fill-mode: forwards;
    }
  `]
})
export class ToastComponent {
  toastService = inject(ToastService);
}