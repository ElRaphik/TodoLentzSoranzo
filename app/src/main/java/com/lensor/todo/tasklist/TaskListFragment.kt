package com.lensor.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.lensor.todo.databinding.FragmentTaskListBinding
import com.lensor.todo.detail.DetailActivity

class TaskListFragment : Fragment() {
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    val adapterListener : TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            taskList = taskList - task;
            refreshAdapter()
        }

        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("task", task)
            editTask.launch(intent)
        }
    }
    private val adapter = TaskListAdapter(adapterListener)
    private lateinit var binding : FragmentTaskListBinding
    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task
        taskList = (taskList + task!!)
        refreshAdapter()
    }
    val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task
        taskList = taskList.map { if (it.id == task.id) task else it }
        refreshAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        val rootView = binding.root
        adapter.submitList(taskList)
        return rootView
    }

    fun refreshAdapter() {
        adapter.submitList(taskList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = adapter
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            createTask.launch(intent)
        }
    }
}