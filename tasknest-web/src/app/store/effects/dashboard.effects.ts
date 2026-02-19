import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { DashboardService } from "../../core/services/dashboard.service";
import { DashboardActions } from "../actions/dashboard.actions";
import { mergeMap, map, catchError, of } from "rxjs";

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
}