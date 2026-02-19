import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Store } from '@ngrx/store';
import { AuthActions } from '../../../store/actions/auth.actions';
import { RouterLink } from '@angular/router';
import { selectAuthLoading, selectAuthError } from '../../../store/selectors/auth.selectors';

@Component({
  selector: 'app-login',
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatInputModule, 
    MatButtonModule, 
    MatCardModule,
    MatProgressSpinnerModule,
    RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private store = inject(Store);

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  isLoading = this.store.selectSignal(selectAuthLoading);
  errorMessage = this.store.selectSignal(selectAuthError);

  onSubmit() {
    if (this.loginForm.valid) {
      this.store.dispatch(AuthActions.login({ request: this.loginForm.value as any }));
    } 
  }
}
