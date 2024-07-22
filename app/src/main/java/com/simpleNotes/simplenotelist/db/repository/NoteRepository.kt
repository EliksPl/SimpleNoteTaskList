package com.simpleNotes.simplenotelist.db.repository

import com.simpleNotes.simplenotelist.model.dbModel.NoteModel
import com.simpleNotes.simplenotelist.model.dbModel.TaskModel
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<NoteModel>>
    fun getAllNotesOnce() : List<NoteModel>
    suspend fun insertNote(note: NoteModel, onSuccess:() -> Unit)
    suspend fun deleteNote(note: NoteModel, onSuccess:() -> Unit)
    suspend fun deleteAllNotes(onSuccess: () -> Unit)

    fun getAllTasks(): Flow<List<TaskModel>>
    fun getAllTasksOnce(): List<TaskModel>
    suspend fun getAllReminders(): List<TaskModel>
    suspend fun insertTask(task: TaskModel)
    suspend fun deleteTask(task: TaskModel)
    suspend fun deleteAllTasks()
}