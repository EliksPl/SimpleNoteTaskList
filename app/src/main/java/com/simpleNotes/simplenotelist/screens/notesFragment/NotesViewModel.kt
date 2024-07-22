package com.simpleNotes.simplenotelist.screens.notesFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.simpleNotes.simplenotelist.REPOSITORY
import com.simpleNotes.simplenotelist.db.NoteDatabase
import com.simpleNotes.simplenotelist.db.repository.NoteRealisation
import com.simpleNotes.simplenotelist.model.dbModel.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): AndroidViewModel(application) {
    val context = application

    fun initDatabase(){
        val daoNote = NoteDatabase.getInstance(context).getNoteDao()
        val daoTask = NoteDatabase.getInstance(context).getTaskDao()
        REPOSITORY = NoteRealisation(daoNote, daoTask)

    }

    fun deleteSelectedNotes(selectedNotes: ArrayList<NoteModel>){
        viewModelScope.launch ( Dispatchers.IO ) {
            for (note in selectedNotes) {
                REPOSITORY.deleteNote(note) {}
            }
        }
    }

    fun allNotes() : Flow<List<NoteModel>> = REPOSITORY.getAllNotes()
}