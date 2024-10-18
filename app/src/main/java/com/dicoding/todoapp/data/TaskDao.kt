package com.dicoding.todoapp.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.sqlite.db.SupportSQLiteQuery

//TODO 2 : Define data access object (DAO)
@Dao
interface TaskDao {


    fun getTasks(query: SupportSQLiteQuery): PagingSource<Int, Task>

    fun getTaskById(taskId: Int): LiveData<Task>

    fun getNearestActiveTask(): Task

    suspend fun insertTask(task: Task): Long

    fun insertAll(vararg tasks: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateCompleted(taskId: Int, completed: Boolean)
    
}
