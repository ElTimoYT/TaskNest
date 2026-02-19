import { createFeatureSelector, createSelector } from "@ngrx/store";
import { DashboardState } from "../reducers/dashboard.reducer";

export const selectDashboardState = createFeatureSelector<DashboardState>('dashboard');
export const selectDashboardData = createSelector(selectDashboardState, (state) => state.data);
export const selectDashboardLoading = createSelector(selectDashboardState, (state) => state.loading);