package com.dicoding.todoapp.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val _nearTask = MutableLiveData<Task>()
    val nearTask: LiveData<Task> = _nearTask

    init {
        getNearestActiveTask()
    }

    private fun getNearestActiveTask() {
        viewModelScope.launch {
            val nearestTask = taskRepository.getNearestActiveTask()
            _nearTask.postValue(nearestTask)
        }
    }

}