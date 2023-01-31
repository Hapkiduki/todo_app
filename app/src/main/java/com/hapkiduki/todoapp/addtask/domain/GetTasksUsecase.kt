package com.hapkiduki.todoapp.addtask.domain

import com.hapkiduki.todoapp.addtask.data.TaskRepository
import com.hapkiduki.todoapp.addtask.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUsecase @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskModel>> = repository.tasks

}