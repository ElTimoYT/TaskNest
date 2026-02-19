import { createActionGroup, emptyProps, props } from "@ngrx/store";
import { AuthResponse, LoginRequest, RegisterRequest } from "../../core/models/auth.models";

export const AuthActions = createActionGroup({
    source: 'Auth API',
    events: {
        'Login': props<{ request: LoginRequest }>(),
        'Login Success': props<{ response: AuthResponse }>(),
        'Login Failure': props<{ error: string }>(),
        'Logout': emptyProps(),

        'Register': props<{ request: RegisterRequest }>(),
        'Register Success': props<{ response: AuthResponse }>(),
        'Register Failure': props<{ error: string }>()
    }
});