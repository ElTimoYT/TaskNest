import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { FormsModule } from '@angular/forms'; 
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Store } from '@ngrx/store';

import confetti from 'canvas-confetti';
import { TaskActions } from '../../../store/actions/task.actions';
import { selectAllTasks, selectTaskLoading, selectTaskError } from '../../../store/selectors/task.selectors';
import { TaskDialogComponent } from '../task-dialog/task-dialog.component';
import { Task } from '../../../core/models/task.model';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import { MatChipsModule } from '@angular/material/chips';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatProgressSpinnerModule, MatDialogModule, MatIconModule, MatTabsModule, MatChipsModule, MatTooltipModule, FormsModule, MatInputModule],
  templateUrl: './task-list.component.html'
})
export class TaskListComponent implements OnInit {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  private toast = inject(ToastService);
  
  
  tasks = this.store.selectSignal(selectAllTasks);
  isLoading = this.store.selectSignal(selectTaskLoading);
  errorMessage = this.store.selectSignal(selectTaskError);

  currentTab = signal<'ALL' | 'TODO' | 'DONE'>('ALL');
  hoveredTask: string | null = null;

  // 👇 NUEVO SIGNAL PARA EL BUSCADOR
  searchQuery = signal<string>('');

  filteredTasks = computed(() => {
    const allTasks = this.tasks();
    const tab = this.currentTab();
    const search = this.searchQuery().toLowerCase().trim();

    // Primero filtramos por pestaña
    let result = allTasks;
    if (tab === 'TODO') result = result.filter(t => t.state !== 'DONE');
    if (tab === 'DONE') result = result.filter(t => t.state === 'DONE');

    // Luego filtramos por el texto del buscador (busca en título o descripción)
    if (search) {
      result = result.filter(t => 
        t.title.toLowerCase().includes(search) || 
        (t.description && t.description.toLowerCase().includes(search))
      );
    }

    return result;
  });

  onSearch(event: Event) {
    const input = event.target as HTMLInputElement;
    this.searchQuery.set(input.value);
  }

  onTabChange(event: any) {
    if ( event.index === 0) this.currentTab.set('ALL');
    else if ( event.index === 1) this.currentTab.set('TODO');
    else if ( event.index === 2) this.currentTab.set('DONE');
  }

  ngOnInit() {
    this.store.dispatch(TaskActions.loadTasks()); 
  }

  
  openNewTaskDialog() {
    const dialogRef = this.dialog.open(TaskDialogComponent, {
    width: '560px',
    maxWidth: '95vw',
    panelClass: 'task-dialog-panel',
  });

    dialogRef.afterClosed().subscribe(result => {
      if (result) this.store.dispatch(TaskActions.createTask({ task: result }));
      this.toast.show(
      '¡Tarea Creada!', 
      `Has creado la nueva tarea.`, 
      'success'
    );
    });

    
  }

  openEditTaskDialog(task: Task) {
    const dialogRef = this.dialog.open(TaskDialogComponent, {
    width: '560px',
    maxWidth: '95vw',
    panelClass: 'task-dialog-panel',
    data: { task }
  });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Le inyectamos el UUID al resultado para que el Backend sepa cuál actualizar
        const updatedTask = { ...result, uuid: task.uuid };
        this.store.dispatch(TaskActions.updateTask({ task: updatedTask }));
       this.toast.show(
      '¡Tarea Actualizada!', 
      `Has actualizado la tarea "${task.title}".`, 
      'success'
    );
      }
    });

    
  }

getTaskShadow(task: any): string {
  if (task.state === 'DONE') return '0 1px 3px rgba(0,0,0,0.1)';

  const isHovered = this.hoveredTask === task.uuid;

  const colors: Record<string, string> = {
    HIGH:   `rgba(244, 63, 94, ${isHovered ? 0.55 : 0.30})`,
    MEDIUM: `rgba(245, 158, 11, ${isHovered ? 0.55 : 0.30})`,
    LOW:    `rgba(16, 185, 129, ${isHovered ? 0.55 : 0.30})`,
  };

  const color = colors[task.priority] ?? 'rgba(99,102,241,0.2)';

  // offset-x positivo empuja la sombra hacia la derecha (lejos del sidebar)
  // spread negativo la hace más contenida
  return `4px 4px 20px -4px ${color}, 2px 2px 8px -2px ${color}`;
}

  deleteTask(uuid: string) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      panelClass: 'rounded-2xl',
      data: {
        title: '⚠️ Eliminar Tarea',
        message: '¿Estás seguro de que deseas eliminar esta tarea? Esta acción no se puede deshacer.',
        confirmText: 'Eliminar',
        color: 'warn' // Botón rojo
      }
    });

    dialogRef.afterClosed().subscribe(confirmado => {
      if (confirmado) {
        this.store.dispatch(TaskActions.deleteTask({ uuid }));
        this.toast.show(
      '¡Tarea Eliminada!', 
      'Has eliminado la tarea.', 
      'success'
    );
      }
    });
  }

  markAsCompleted(task: Task) {

    this.triggerConfetti();

    this.toast.show(
      '¡Tarea Completada!', 
      `Has finalizado "${task.title}". ¡Sigue así!`, 
      'success'
    );

    const updatedTask = { ...task, state: 'DONE' as const };
    this.store.dispatch(TaskActions.updateTask({ task: updatedTask }));
  }

  private triggerConfetti() {
    // Colores corporativos de tu app (Índigo, Púrpura, Rosa y un toque Esmeralda)
    const brandColors = ['#6366f1', '#a855f7', '#ec4899', '#10b981'];

    // Explosión central elegante
    confetti({
      particleCount: 120,    // Cantidad de confeti (ni muy pobre, ni muy exagerado)
      spread: 80,            // Amplitud de la explosión
      origin: { y: 0.6 },    // Nace un poco más abajo del centro de la pantalla
      colors: brandColors,   // Usa nuestros colores
      zIndex: 9999,          // Se asegura de estar por encima de los modales o navbar
      disableForReducedMotion: true // Accesibilidad: se desactiva si el usuario tiene mareos en su SO
    });

    // Pequeño truco Asana: una segunda explosión más suave a los 200ms para dar efecto de profundidad
    setTimeout(() => {
      confetti({
        particleCount: 50,
        spread: 100,
        origin: { y: 0.6 },
        colors: brandColors,
        startVelocity: 20, // Sale más despacio
        zIndex: 9999
      });
    }, 200);
  }

}