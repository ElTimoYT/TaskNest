import { createReducer, on } from "@ngrx/store";
import { Task } from "../../core/models/task.model";
import { TaskActions } from "../actions/task.actions";

export interface TaskState {
  tasks: Task[];
  loading: boolean;
  error: string | null;
}

export const initialState: TaskState = {
    tasks: [],
    loading: false,
    error: null
}

export const taskReducer = createReducer(
    initialState,

    on(TaskActions.loadTasks, state => ({ ...state, loading: true, error: null })),
    on(TaskActions.loadTasksSuccess, (state, { tasks }) => ({ ...state, tasks, loading: false })),
    on(TaskActions.loadTasksFailure, (state, { error }) => ({ ...state, error, loading: false })),
  
    // Crear tarea (Añadimos la nueva tarea a la lista directamente sin recargar)
    on(TaskActions.createTask, state => ({ ...state, loading: true, error: null })),
    on(TaskActions.createTaskSuccess, (state, { task }) => ({ ...state, tasks: [...state.tasks, task], loading: false })),
    on(TaskActions.createTaskFailure, (state, { error }) => ({ ...state, error, loading: false })),

    on(TaskActions.updateTaskSuccess, (state, { task }) => ({
    ...state,
    tasks: state.tasks.map(t => t.uuid === task.uuid ? task : t),
    loading: false
  })),

  // Borrar Tarea (Filtramos la lista para quitar la borrada)
  on(TaskActions.deleteTaskSuccess, (state, { uuid }) => ({
    ...state,
    tasks: state.tasks.filter(t => t.uuid !== uuid),
    loading: false
  }))
);