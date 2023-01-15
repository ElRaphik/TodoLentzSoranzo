package com.example.todo_soranzo_lentz.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_soranzo_lentz.detail.ui.theme.ToDoSoranzoLentzTheme
import com.example.todo_soranzo_lentz.list.Task
import java.util.*

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newTask = Task(id = UUID.randomUUID().toString(), title = "New Task !")
        val task = intent.getSerializableExtra("task") as Task?
        val onValidate = { task : Task? -> Unit
            intent.putExtra("task", task);
            setResult(RESULT_OK, intent);
            finish()
        }
        setContent {
            ToDoSoranzoLentzTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    if (task == null) Detail(onValidate, newTask)
                    else Detail(onValidate, task)
                }
            }
        }
    }
}

@Composable
fun Detail(onValidate: (Task?) -> Unit, initialTask: Task?) {
    var task by remember { mutableStateOf(initialTask) } // faire les imports suggérés par l'IDE
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Task Detail", style = MaterialTheme.typography.h3)
        OutlinedTextField(value = task?.id ?: UUID.randomUUID().toString(), onValueChange = {task = task?.copy(id = it)}, label = { Text(text = "Id")})
        OutlinedTextField(value = task?.title ?:"New task", onValueChange = {task = task?.copy(title = it)}, label = { Text(text = "Title")})
        OutlinedTextField(value = task?.description ?:"This is a new task", onValueChange = {task = task?.copy(description = it)}, label = { Text(text = "Description")})
        Button(onClick = { onValidate(task) }) {
            Text("Validate")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    ToDoSoranzoLentzTheme {
        //Detail("Android")
    }
}