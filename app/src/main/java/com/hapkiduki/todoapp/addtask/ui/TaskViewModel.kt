package com.hapkiduki.todoapp.addtask.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkiduki.todoapp.addtask.domain.AddTaskUsecase
import com.hapkiduki.todoapp.addtask.domain.DeleteTaskUseCase
import com.hapkiduki.todoapp.addtask.domain.GetTasksUsecase
import com.hapkiduki.todoapp.addtask.domain.UpdateTaskUsecase
import com.hapkiduki.todoapp.addtask.ui.TaskUiState.*
import com.hapkiduki.todoapp.addtask.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val updateTaskUsecase: UpdateTaskUsecase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val addTaskUsecase: AddTaskUsecase,
    getTasksUsecase: GetTasksUsecase
) : ViewModel() {

    val uiState: StateFlow<TaskUiState> = getTasksUsecase().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: MutableLiveData<Boolean> = _showDialog

    private val _tasks = mutableStateListOf<TaskModel>()
    val tasks: List<TaskModel> = _tasks

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onTaskCreated(task: String) {
        onDialogClose()

        viewModelScope.launch {
            addTaskUsecase(TaskModel(task = task))
        }
    }

    fun showDialogScreen() {
        _showDialog.value = true
    }

    fun onTaskSelected(task: TaskModel) {
        viewModelScope.launch {
            updateTaskUsecase(task.copy(selected = !task.selected))
        }
    }

    fun onTaskRemoved(task: TaskModel) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
        }
    }

}