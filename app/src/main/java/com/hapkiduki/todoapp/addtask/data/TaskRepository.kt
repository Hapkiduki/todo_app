package com.hapkiduki.todoapp.addtask.data

import com.hapkiduki.todoapp.addtask.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
    val tasks: Flow<List<TaskModel>> =
        taskDao.getTask()
            .map { items -> items.map { TaskModel(it.id, it.task, it.selected) } }


    suspend fun add(taskModel: TaskModel) {
        taskDao.addTask(
            TaskEntity(
                taskModel.id,
                taskModel.task,
                taskModel.selected
            )
        )
    }

    suspend fun update(task: TaskModel) {
        taskDao.updateTask(
            TaskEntity(
                task.id,
                task.task,
                task.selected
            )
        )
    }

    suspend fun delete(task: TaskModel) {
        taskDao.deleteTask(
            TaskEntity(
                task.id,
                task.task,
                task.selected
            )
        )
    }
}