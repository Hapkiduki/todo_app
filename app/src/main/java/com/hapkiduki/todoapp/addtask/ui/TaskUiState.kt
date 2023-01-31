package com.hapkiduki.todoapp.addtask.ui

import com.hapkiduki.todoapp.addtask.ui.model.TaskModel

sealed interface TaskUiState {
    object Loading: TaskUiState
    data class Error(val throwable: Throwable) : TaskUiState
    data class Success(val data: List<TaskModel>) : TaskUiState
}