import { createReducer, on } from "@ngrx/store";
import { User } from "../../core/models/user.model";
import { AuthActions } from "../actions/auth.actions";

export interface AuthState {
    user: User | null;
    token: string | null;
    loading: boolean;
    error: string | null;
}

export const initialState: AuthState = {
    user: null,
    token: localStorage.getItem('token'),
    loading: false,
    error: null
};

export const authReducer = createReducer(
    initialState,

    on(AuthActions.login, (state) => ({
        ...state,
        loading: true,
        error: null
    })),

    on(AuthActions.loginSuccess, (state, { response }) => ({
        ...state,
        token: response.token,
        loading: false,
        error: null
    })),
    on(AuthActions.loginFailure, (state, { error }) => ({
        ...state,
        loading: false,
        error: error
    })),
    on(AuthActions.logout, (state) => ({
        ...state,
        user: null,
        token: null
    })),
    on(AuthActions.register, (state) => ({
        ...state,
        loading: true,
        error: null
    })),
    on(AuthActions.registerSuccess, (state, { response }) => ({
        ...state,
        token: response.token,
        loading: false,
        error: null
    })),
    on(AuthActions.registerFailure, (state, { error }) => ({
        ...state,
        loading: false,
        error: error
    }))
);