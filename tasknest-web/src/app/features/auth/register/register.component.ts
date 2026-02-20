import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { selectAuthError, selectAuthLoading } from '../../../store/selectors/auth.selectors';
import { AuthActions } from '../../../store/actions/auth.actions';
import { MatCardModule } from "@angular/material/card";
import { MatInputModule } from "@angular/material/input";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [CommonModule, ReactiveFormsModule, MatInputModule, 
            MatButtonModule, MatCardModule, MatProgressSpinnerModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  private fb = inject(FormBuilder);
  private store = inject(Store);

  registerForm = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    username: ['', [Validators.required, Validators.minLength(4)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  isLoading = this.store.selectSignal(selectAuthLoading);
  errorMessage = this.store.selectSignal(selectAuthError);

  onSubmit() {
    if (this.registerForm.valid) {
      this.store.dispatch(AuthActions.register({ request: this.registerForm.getRawValue() }));
    }
  }

}
