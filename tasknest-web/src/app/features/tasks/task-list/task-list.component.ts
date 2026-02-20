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


import { TaskActions } from '../../../store/actions/task.actions';
import { selectAllTasks, selectTaskLoading, selectTaskError } from '../../../store/selectors/task.selectors';
import { TaskDialogComponent } from '../task-dialog/task-dialog.component';
import { Task } from '../../../core/models/task.model';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import { MatChipsModule } from '@angular/material/chips';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatProgressSpinnerModule, MatDialogModule, MatIconModule, MatTabsModule, MatChipsModule, MatTooltipModule, FormsModule, MatInputModule],
  templateUrl: './task-list.component.html'
})
export class TaskListComponent implements OnInit {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  
  
  tasks = this.store.selectSignal(selectAllTasks);
  isLoading = this.store.selectSignal(selectTaskLoading);
  errorMessage = this.store.selectSignal(selectTaskError);

  currentTab = signal<'ALL' | 'TODO' | 'DONE'>('ALL');

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
      width: '500px', disableClose: true, panelClass: 'rounded-2xl'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) this.store.dispatch(TaskActions.createTask({ task: result }));
    });
  }

  openEditTaskDialog(task: Task) {
    const dialogRef = this.dialog.open(TaskDialogComponent, {
      width: '500px',
      disableClose: true,
      panelClass: 'rounded-2xl',
      data: { task } // Le pasamos la tarea actual al modal
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Le inyectamos el UUID al resultado para que el Backend sepa cuál actualizar
        const updatedTask = { ...result, uuid: task.uuid };
        this.store.dispatch(TaskActions.updateTask({ task: updatedTask }));
      }
    });
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
      }
    });
  }

  markAsCompleted(task: Task) {
    const updatedTask = { ...task, state: 'DONE' as const };
    this.store.dispatch(TaskActions.updateTask({ task: updatedTask }));
  }

}