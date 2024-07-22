package com.simpleNotes.simplenotelist.db.dao

import androidx.room.*
import com.simpleNotes.simplenotelist.TASK_DB_TABLE_NAME
import com.simpleNotes.simplenotelist.model.dbModel.TaskModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: TaskModel)
    @Delete
    suspend fun deleteTask(task: TaskModel)

    @Query("SELECT * from $TASK_DB_TABLE_NAME")
    fun getAllTasks(): Flow<List<TaskModel>>

    @Query("SELECT * from $TASK_DB_TABLE_NAME")
    fun getAllTasksOnce(): List<TaskModel>

    @Query("SELECT * from $TASK_DB_TABLE_NAME WHERE remind_on_date > 0")
    fun getAllReminders(): List<TaskModel>

    @Query("DELETE from $TASK_DB_TABLE_NAME")
    suspend fun deleteAllTasks()
}