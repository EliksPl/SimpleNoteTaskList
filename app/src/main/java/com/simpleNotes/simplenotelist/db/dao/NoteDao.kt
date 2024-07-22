package com.simpleNotes.simplenotelist.db.dao

import androidx.room.*
import com.simpleNotes.simplenotelist.NOTE_DB_TABLE_NAME
import com.simpleNotes.simplenotelist.model.dbModel.NoteModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: NoteModel)
    @Delete
    suspend fun deleteNote(note: NoteModel)

    @Query("SELECT * from $NOTE_DB_TABLE_NAME")
    fun getAllNotes(): Flow<List<NoteModel>>

    @Query("SELECT * from $NOTE_DB_TABLE_NAME")
    fun getAllNotesOnce(): List<NoteModel>

    @Query("DELETE from $NOTE_DB_TABLE_NAME")
    suspend fun deleteAllNotes()
}