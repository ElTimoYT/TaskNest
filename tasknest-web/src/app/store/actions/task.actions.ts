import { createActionGroup, emptyProps, props } from "@ngrx/store";
import { Task, TaskRequest } from "../../core/models/task.model";

export const TaskActions = createActionGroup({
    source: 'Task',
    events: {
        'Load Tasks': emptyProps(),
        'Load Tasks Success': props<{ tasks: Task[] }>(),
        'Load Tasks Failure': props<{ error: string }>(),
    
        'Create Task': props<{ task: TaskRequest }>(),
        'Create Task Success': props<{ task: Task }>(),
        'Create Task Failure': props<{ error: string }>(),

        'Update Task': props<{ task: Task }>(),
        'Update Task Success': props<{ task: Task }>(),
        'Update Task Failure': props<{ error: string }>(),

        'Delete Task': props<{ uuid: string }>(),
        'Delete Task Success': props<{ uuid: string }>(),
        'Delete Task Failure': props<{ error: string }>(),
    }
});