package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.databinding.ActivityTaskDetailBinding
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.list.TaskViewModel
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailBinding
    private lateinit var taskViewModel: DetailTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //TODO 11 : Show detail task and implement delete action

        val factory = ViewModelFactory.getInstance(this)
        taskViewModel = ViewModelProvider(this, factory)[DetailTaskViewModel::class.java]

        val taskId = intent.getIntExtra(TASK_ID, -1)
        taskViewModel.setTaskId(taskId)

        taskViewModel.task.observe(this) {
            showDetailTask(it)
        }

    }

    private fun showDetailTask(task: Task) {
        binding.apply {
            detailEdTitle.setText(task.title)
            detailEdDescription.setText(task.description)
            detailEdDueDate.setText(DateConverter.convertMillisToString(task.dueDateMillis))
        }
    }

    private fun deleteTask() {
        binding.btnDeleteTask.setOnClickListener {
            taskViewModel.deleteTask()
        }
    }

}