import { createAction, createActionGroup, emptyProps, props } from "@ngrx/store";
import { DashboardData } from "../../core/models/dashboard.model";

export const DashboardActions = createActionGroup({
    source: 'Dashboard API',
    events: {
        'Load Dashboard': emptyProps(),
        'Load Dashboard Success': props<{ data: DashboardData }>(),
        'Load Dashboard Failure': props<{ error: string }>()
    }
});