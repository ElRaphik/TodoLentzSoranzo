package com.example.todo_soranzo_lentz.list

data class Task (val id : String, val title: String, val description: String = "default description") : java.io.Serializable