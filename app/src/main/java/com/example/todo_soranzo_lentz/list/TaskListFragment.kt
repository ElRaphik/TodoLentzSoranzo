package com.example.todo_soranzo_lentz.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.todo_soranzo_lentz.R
import com.example.todo_soranzo_lentz.data.Api
import com.example.todo_soranzo_lentz.databinding.FragmentTaskListBinding
import com.example.todo_soranzo_lentz.detail.DetailActivity
import coil.load
import coil.transform.CircleCropTransformation
import com.example.todo_soranzo_lentz.user.UserActivity
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    /*private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )*/
    val adapterListener : TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            /*taskList = taskList - task;
            refreshAdapter()*/
            viewModel.remove(task)
            viewModel.refresh()
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
        /*taskList = (taskList + task!!)
        refreshAdapter()*/
        viewModel.add(task)
        viewModel.refresh()
    }
    val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task
        /*taskList = taskList.map { if (it.id == task.id) task else it }
        refreshAdapter()*/
        viewModel.edit(task)
        viewModel.refresh()
    }

    val editProfile = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> }

    private val viewModel: TasksListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        val rootView = binding.root
        //adapter.submitList(taskList)
        return rootView
    }

    /*fun refreshAdapter() {
        adapter.submitList(taskList)
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = adapter
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            createTask.launch(intent)
        }
        binding.imageView.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            editTask.launch(intent)
        }
        viewModel.refresh()
        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                // cette lambda est executée à chaque fois que la liste est mise à jour dans le VM
                /*taskList = newList
                refreshAdapter()*/
                adapter.submitList(newList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            mySuspendMethod()
        }
        viewModel.refresh()
        /*view?.findViewById<ImageView>(R.id.imageView)?.load("https://goo.gl/gEgYUd") {
            transformations(CircleCropTransformation())
        }*/
    }

    private suspend fun mySuspendMethod() {
        val user = Api.userWebService.fetchUser().body()!!
        view?.findViewById<TextView>(R.id.userTextView)?.text = user.name
        view?.findViewById<ImageView>(R.id.imageView)?.load(user.avatar) {
            error(R.drawable.ic_launcher_background) // image par défaut en cas d'erreur
        }
    }
}