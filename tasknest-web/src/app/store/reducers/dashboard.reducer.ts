import { createReducer, on } from "@ngrx/store";
import { DashboardData } from "../../core/models/dashboard.model";
import { DashboardActions } from "../actions/dashboard.actions";

export interface DashboardState {
    data: DashboardData | null;
    loading: boolean;
    error: string | null;
}

export const initialState: DashboardState = {
    data: null,
    loading: false,
    error: null
};

export const dashboardReducer = createReducer(
    initialState,
    on(DashboardActions.loadDashboard, (state) => ({
        ...state,
        loading: true,
        error: null 
    })),
    on(DashboardActions.loadDashboardSuccess, (state, { data }) => ({
        ...state,
        data: data,
        loading: false
    })),
    on(DashboardActions.loadDashboardFailure, (state, { error }) => ({
        ...state,
        loading: false,
        error: error
    }))
        
)