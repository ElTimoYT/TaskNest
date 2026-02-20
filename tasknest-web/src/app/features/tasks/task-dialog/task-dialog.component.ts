import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule, MatChipInputEvent } from '@angular/material/chips';
import { MatDialogRef, MatDialogModule, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ENTER, COMMA } from '@angular/cdk/keycodes';
import { Task } from '../../../core/models/task.model';

@Component({
  selector: 'app-task-dialog',
  imports: [CommonModule, ReactiveFormsModule, MatDialogModule, MatInputModule, MatButtonModule, MatSelectModule, MatIconModule, MatChipsModule],
  templateUrl: './task-dialog.component.html',
  styleUrl: './task-dialog.component.scss'
})
export class TaskDialogComponent implements OnInit{

  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<TaskDialogComponent>);

  data = inject(MAT_DIALOG_DATA) as { task?: Task } | null;

  tags: string[] = [];
  readonly separatorKeysCodes = [ENTER, COMMA] as const; // Enter y Coma

  taskForm = this.fb.nonNullable.group({
    title: ['', [Validators.required, Validators.maxLength(100)]],
    description: [''],
    dueDate: ['', Validators.required],
    priority: ['MEDIUM', Validators.required],
  });

  ngOnInit() {
    if (this.data?.task) {
      this.taskForm.patchValue({
        title: this.data.task.title,
        description: this.data.task.description,
        dueDate: this.data.task.dueDate,
        priority: this.data.task.priority,
      });
      if (this.data.task.tags) {
        this.tags = [...this.data.task.tags];
      }
    }
  }

  addTag(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    if (value && !this.tags.includes(value)) {
      this.tags.push(value);
    } 
    event.chipInput!.clear();
  }

  removeTag(tag: string): void {
    const index = this.tags.indexOf(tag);
    if (index >= 0) {
      this.tags.splice(index, 1);
    }
  }

  onCancel() {
    this.dialogRef.close();
  }

  onSubmit() {
    if (this.taskForm.valid) {
      // Sacamos los datos crudos del formulario
      const formValue = this.taskForm.getRawValue();
      
      // Creamos el objeto exactamente como lo espera Spring Boot
      const taskRequest = {
        title: formValue.title,
        description: formValue.description,
        dueDate: formValue.dueDate, // Ya viene como "YYYY-MM-DD" del input type="date"
        priority: formValue.priority,
        state: 'TODO', // Por defecto, la nueva tarea siempre empieza en TODO
        notes: '', // Por ahora dejamos las notas vacías
        tags: this.tags
      };

      console.log("Datos que se envían al Backend:", taskRequest);
      this.dialogRef.close(taskRequest);
    }
  }

}
