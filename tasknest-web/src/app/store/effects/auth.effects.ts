import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { AuthService } from "../../core/services/auth.service";
import { Router } from "@angular/router";
import { AuthActions } from "../actions/auth.actions";
import { catchError, map, mergeMap, of, tap } from "rxjs";

@Injectable()
export class AuthEffects {
    private actions$ = inject(Actions);
    private authService = inject(AuthService);
    private router = inject(Router);

    login$ = createEffect(() => 
        this.actions$.pipe(
            ofType(AuthActions.login), // Escucha: ¡Alguien quiere loguearse!
            mergeMap((action) => 
                this.authService.login(action.request).pipe(
                    // Si sale bien -> Despacha Success
                    map((response) => AuthActions.loginSuccess({ response })),
                    // Si sale mal -> Despacha Failure con el mensaje
                    catchError((error) => of(AuthActions.loginFailure({ error: error.error.message || 'Error desconocido' })))
                )
            )
        )
    );

    loginSuccess$ = createEffect(() => 
        this.actions$.pipe(
            ofType(AuthActions.loginSuccess), // Escucha: ¡Login exitoso!
            tap(({response}) => {
                // Guarda el token en localStorage
                localStorage.setItem('token', response.token);
                // Redirige al dashboard
                this.router.navigate(['/dashboard']);
            })
        ),
        { dispatch: false } // No despacha ninguna acción
    );

    register$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.register),
      mergeMap((action) =>
        this.authService.register(action.request).pipe(
          map((response) => AuthActions.registerSuccess({ response })),
          catchError((error) => of(AuthActions.registerFailure({ error: error.error?.message || 'Error en el registro' })))
        )
      )
    )
  );

  // Efecto tras Register Exitoso (Igual que el login, guardamos token y navegamos)
  registerSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.registerSuccess),
      tap(({ response }) => {
        localStorage.setItem('token', response.token);
        this.router.navigate(['/dashboard']);
      })
    ),
    { dispatch: false }
  );
}