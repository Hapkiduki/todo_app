package com.hapkiduki.todoapp.addtask.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.hapkiduki.todoapp.addtask.ui.model.TaskModel
import kotlinx.coroutines.flow.collect

@Composable
fun TaskScreen(taskViewModel: TaskViewModel = hiltViewModel()) {

    val showDialog: Boolean by taskViewModel.showDialog.observeAsState(false)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<TaskUiState>(
        initialValue = TaskUiState.Loading,
        key1 = lifecycle,
        key2 = taskViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            taskViewModel.uiState.collect { value = it}
        }
    }

    when(uiState) {
        is TaskUiState.Error -> {}
        TaskUiState.Loading -> {
            CircularProgressIndicator()
        }
        is TaskUiState.Success -> {
            Box(modifier = Modifier.fillMaxSize()) {
                AddTaskDialog(show = showDialog,
                    onTaskAdded = {
                        taskViewModel.onTaskCreated(it)
                    },
                    onDismiss = { taskViewModel.onDialogClose() })
                FabDialog(Modifier.align(Alignment.BottomEnd),
                    onPressed = {
                        taskViewModel.showDialogScreen()
                    })
                TaskList(
                    myTasks = (uiState as TaskUiState.Success).data,
                    onItemSelected = {
                        taskViewModel.onTaskSelected(it)
                    },
                    onItemRemoved = {
                        taskViewModel.onTaskRemoved(it)
                    }
                )
            }
        }
    }


}

@Composable
fun TaskList(
    myTasks: List<TaskModel> = emptyList(),
    onItemSelected: (TaskModel) -> Unit,
    onItemRemoved: (TaskModel) -> Unit
) {


    LazyColumn {
        items(myTasks, key = { it.id }) { task ->
            ItemTask(
                taskModel = task,
                onItemSelected = onItemSelected,
                onItemRemoved = onItemRemoved
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemTask(
    taskModel: TaskModel,
    onItemSelected: (TaskModel) -> Unit,
    onItemRemoved: (TaskModel) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onItemRemoved(taskModel)
                    }
                )
            },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = taskModel.task, modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(1f)
            )
            Checkbox(checked = taskModel.selected, onCheckedChange = {
                onItemSelected(taskModel)
            })
        }
    }
}

@Composable
fun FabDialog(modifier: Modifier, onPressed: () -> Unit) {
    FloatingActionButton(
        onClick = onPressed,
        modifier = modifier
            .padding(16.dp)
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "")
    }
}

@Composable
fun AddTaskDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onTaskAdded: (String) -> Unit
) {
    var myTask by remember {
        mutableStateOf("")
    }

    if (show) {
        Dialog(onDismissRequest = onDismiss) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Añade tu tarea",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = myTask,
                    onValueChange = {
                        myTask = it
                    },
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    onClick = {
                        onTaskAdded(myTask)
                        myTask = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Añadir tarea")
                }
            }
        }
    }
}
