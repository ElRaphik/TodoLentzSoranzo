package com.lensor.todo.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lensor.todo.R

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
}

object TasksDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return (oldItem.title == newItem.title) && (oldItem.description == newItem.description)
    }
}

class TaskListAdapter(val listener: TaskListListener) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TasksDiffCallback) {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            val textView = itemView.findViewById<TextView>(R.id.task_title)
            textView.text = task.title
            val secondTextView = itemView.findViewById<TextView>(R.id.task_description)
            secondTextView.text = task.description
            val deleteButton = itemView.findViewById<ImageButton>(R.id.imageButton2)
            deleteButton.setOnClickListener { listener.onClickDelete(task) }
            val editButton = itemView.findViewById<ImageButton>(R.id.imageButton)
            editButton.setOnClickListener { listener.onClickEdit(task) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}