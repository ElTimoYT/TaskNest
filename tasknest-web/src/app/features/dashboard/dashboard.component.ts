import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { Store } from '@ngrx/store';
import { DashboardActions } from '../../store/actions/dashboard.actions';
import { selectDashboardData, selectDashboardLoading } from '../../store/selectors/dashboard.selectors';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, MatCardModule, MatProgressBarModule, MatProgressSpinnerModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  private store = inject(Store);

  dashboardData = this.store.selectSignal(selectDashboardData);
  isLoading = this.store.selectSignal(selectDashboardLoading);

  ngOnInit() {
    this.store.dispatch(DashboardActions.loadDashboard());
  }

  getStatusColor(status: string | undefined): string {
    switch (status) {
      case 'LIGHT': return 'bg-emerald-100 text-emerald-800 border-emerald-300'; // 🟢
      case 'MODERATE': return 'bg-amber-100 text-amber-800 border-amber-300';   // 🟡
      case 'HEAVY': return 'bg-rose-100 text-rose-800 border-rose-300';         // 🔴
      case 'BURNOUT': return 'bg-purple-100 text-purple-800 border-purple-300'; // 🔥
      default: return 'bg-gray-100 text-gray-800 border-gray-300';
    }
  }

}
