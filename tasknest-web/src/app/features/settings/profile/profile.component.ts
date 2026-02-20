import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatSnackBarModule, MatProgressSpinnerModule],
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private snackBar = inject(MatSnackBar);

  userInitials: string = 'US';
  usernameDisplay: string = 'Usuario';
  isLoading = false;

  // Formulario Reactivo
  passwordForm = this.fb.nonNullable.group({
    currentPassword: ['', Validators.required],
    newPassword: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', Validators.required]
  }, { validators: this.passwordsMatchValidator });

  ngOnInit() {
    this.extractUserData();
  }

  // Validador personalizado para comprobar que las contraseñas coinciden
  passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
    const newPassword = control.get('newPassword')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    return newPassword === confirmPassword ? null : { passwordsMismatch: true };
  }

  // Extraemos datos del JWT para mostrarlos bonitos
  extractUserData() {
    const token = localStorage.getItem('token');
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      this.usernameDisplay = payload.sub || payload.email || 'Usuario';
      this.userInitials = this.usernameDisplay.substring(0, 2).toUpperCase();
    }
  }

  onSubmit() {
    if (this.passwordForm.invalid) return;

    this.isLoading = true;
    const { currentPassword, newPassword } = this.passwordForm.getRawValue();

    this.authService.changePassword(currentPassword, newPassword).subscribe({
      next: () => {
        this.isLoading = false;
        this.passwordForm.reset();
        this.snackBar.open('✅ Contraseña actualizada con éxito', 'Cerrar', { duration: 3000 });
      },
      error: (err) => {
        this.isLoading = false;
        const errorMsg = err.error?.error || 'Error al cambiar la contraseña';
        this.snackBar.open('❌ ' + errorMsg, 'Cerrar', { duration: 5000 });
      }
    });
  }
}