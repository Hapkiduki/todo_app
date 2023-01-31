package com.hapkiduki.todoapp.addtask.domain

import com.hapkiduki.todoapp.addtask.data.TaskRepository
import com.hapkiduki.todoapp.addtask.ui.model.TaskModel
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(task: TaskModel) {
        repository.delete(task)
    }
}