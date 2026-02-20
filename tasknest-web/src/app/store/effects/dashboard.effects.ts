import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { DashboardService } from "../../core/services/dashboard.service";
import { DashboardActions } from "../actions/dashboard.actions";
import { mergeMap, map, catchError, of } from "rxjs";
import { TaskActions } from "../actions/task.actions";

@Injectable()
export class DashboardEffects {

    private actions$ = inject(Actions);
    private dashboardService = inject(DashboardService);

    loadDashboard$ = createEffect(() =>
    this.actions$.pipe(
      ofType(DashboardActions.loadDashboard),
      mergeMap(() =>
        this.dashboardService.getDashboardStats().pipe(
          map(data => DashboardActions.loadDashboardSuccess({ data })),
          catchError(error => of(DashboardActions.loadDashboardFailure({ error: error.message })))
        )
      )
    )
  );

  refreshDashboard$ = createEffect(() =>
    this.actions$.pipe(
      // Si ocurre CUALQUIERA de estas tres acciones de éxito...
      ofType(
        TaskActions.createTaskSuccess,
        TaskActions.updateTaskSuccess,
        TaskActions.deleteTaskSuccess
      ),
      // ... disparamos la orden de recargar el Dashboard en segundo plano
      map(() => DashboardActions.loadDashboard())
    )
  );
}