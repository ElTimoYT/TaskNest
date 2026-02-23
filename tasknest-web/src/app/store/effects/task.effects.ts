import { Injectable, inject } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { mergeMap, map, catchError, of, tap } from "rxjs";
import { TaskService } from "../../core/services/task.service";
import { TaskActions } from "../actions/task.actions";
import { MatSnackBar } from '@angular/material/snack-bar';
import { ToastService } from "../../core/services/toast.service";

@Injectable()
export class TaskEffects {
  private actions$ = inject(Actions);
  private taskService = inject(TaskService);
  private snackBar = inject(MatSnackBar);
  private toast = inject(ToastService);
  

  loadTasks$ = createEffect(() =>
    this.actions$.pipe(
      ofType(TaskActions.loadTasks),
      mergeMap(() => this.taskService.getTasks().pipe(
        map(tasks => TaskActions.loadTasksSuccess({ tasks })),
        catchError(error => of(TaskActions.loadTasksFailure({ error: error.message })))
      ))
    )
  );

  createTask$ = createEffect(() =>
    this.actions$.pipe(
      ofType(TaskActions.createTask),
      mergeMap(action => this.taskService.createTask(action.task).pipe(
        map(task => TaskActions.createTaskSuccess({ task })),
        catchError(error => of(TaskActions.createTaskFailure({ error: error.message })))
      ))
    )
  );

  updateTask$ = createEffect(() =>
    this.actions$.pipe(
      ofType(TaskActions.updateTask),
      mergeMap(action => this.taskService.updateTask(action.task.uuid, action.task).pipe(
        map(task => TaskActions.updateTaskSuccess({ task })),
        catchError(error => of(TaskActions.updateTaskFailure({ error: error.message })))
      ))
    )
  );

  deleteTask$ = createEffect(() =>
    this.actions$.pipe(
      ofType(TaskActions.deleteTask),
      mergeMap(action => this.taskService.deleteTask(action.uuid).pipe(
        // En el borrado, devolvemos el mismo UUID para que el reducer sepa a quién matar
        map(() => TaskActions.deleteTaskSuccess({ uuid: action.uuid })), 
        catchError(error => of(TaskActions.deleteTaskFailure({ error: error.message })))
      ))
    )
  );
}