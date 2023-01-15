package com.lensor.todo.tasklist

data class Task (val id : String, val title: String, val description: String = "default description") : java.io.Serializable
