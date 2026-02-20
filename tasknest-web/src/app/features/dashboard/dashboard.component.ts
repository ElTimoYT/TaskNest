import { Component, OnInit, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Store } from '@ngrx/store';

// Importamos los selectores y acciones de las TAREAS directamente
import { selectAllTasks, selectTaskLoading } from '../../store/selectors/task.selectors';
import { TaskActions } from '../../store/actions/task.actions';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule, MatProgressSpinnerModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  private store = inject(Store);

  // 1. Traemos las tareas directamente de la memoria de Redux
  tasks = this.store.selectSignal(selectAllTasks);
  isLoading = this.store.selectSignal(selectTaskLoading);

  // 2. Calculamos TODO mágicamente usando Signals (Busca el estado 'DONE')
  stats = computed(() => {
    const allTasks = this.tasks();
    const total = allTasks.length;
    const completed = allTasks.filter(t => t.state === 'DONE').length;
    const pending = total - completed;
    
    // Evitamos dividir por cero si no hay tareas
    const percentage = total === 0 ? 0 : Math.round((completed / total) * 100);

    return { total, completed, pending, percentage };
  });

  ngOnInit() {
    // Solo le decimos a Redux que se asegure de cargar las tareas
    this.store.dispatch(TaskActions.loadTasks());
  }
}
