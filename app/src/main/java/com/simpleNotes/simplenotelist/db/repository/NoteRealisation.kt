package com.simpleNotes.simplenotelist.db.repository

import com.simpleNotes.simplenotelist.db.dao.NoteDao
import com.simpleNotes.simplenotelist.db.dao.TaskDao
import com.simpleNotes.simplenotelist.model.dbModel.NoteModel
import com.simpleNotes.simplenotelist.model.dbModel.TaskModel
import kotlinx.coroutines.flow.Flow

class NoteRealisation(private val noteDao: NoteDao, private val taskDao: TaskDao): NoteRepository {
    override fun getAllNotes(): Flow<List<NoteModel>> {
        return noteDao.getAllNotes()
    }

    override fun getAllNotesOnce(): List<NoteModel> {
        return noteDao.getAllNotesOnce()
    }

    override suspend fun insertNote(note: NoteModel, onSuccess: () -> Unit) {
        noteDao.insertNote(note)
        onSuccess()
    }

    override suspend fun deleteNote(note: NoteModel, onSuccess: () -> Unit) {
        noteDao.deleteNote(note)
        onSuccess()
    }

    override suspend fun deleteAllNotes(onSuccess: () -> Unit) {
        noteDao.deleteAllNotes()
        onSuccess()
    }

    override fun getAllTasks(): Flow<List<TaskModel>> {
        return taskDao.getAllTasks()
    }

    override fun getAllTasksOnce(): List<TaskModel> {
        return taskDao.getAllTasksOnce()
    }

    override suspend fun getAllReminders(): List<TaskModel>{
        return taskDao.getAllReminders()
    }

    override suspend fun insertTask(task: TaskModel) {
        taskDao.insertTask(task)
    }

    override suspend fun deleteTask(task: TaskModel) {
        taskDao.deleteTask(task)
    }

    override suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }
}