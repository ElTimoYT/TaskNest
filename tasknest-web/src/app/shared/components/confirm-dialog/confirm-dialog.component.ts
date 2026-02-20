import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule],
  template: `
    <div class="p-6">
      <h2 mat-dialog-title class="!p-0 text-2xl font-bold text-gray-800 flex items-center gap-2 mb-4">
        {{ data.title }}
      </h2>
      
      <mat-dialog-content class="!p-0">
        <p class="text-gray-600 text-base">{{ data.message }}</p>
      </mat-dialog-content>
      
      <mat-dialog-actions align="end" class="!p-0 mt-6">
        <button mat-stroked-button mat-dialog-close class="mr-2">Cancelar</button>
        <button mat-flat-button [color]="data.color || 'primary'" [mat-dialog-close]="true">
          {{ data.confirmText }}
        </button>
      </mat-dialog-actions>
    </div>
  `
})
export class ConfirmDialogComponent {
  // Inyectamos los datos que le pasaremos desde fuera (título, mensaje, etc.)
  data = inject(MAT_DIALOG_DATA);
}
