package com.simpleNotes.simplenotelist.screens.descriptionFragment

import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simpleNotes.simplenotelist.REPOSITORY
import com.simpleNotes.simplenotelist.model.dbModel.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteDescriptionViewModel : ViewModel() {
    fun tryDeleteNote(note: NoteModel, onSuccess:()-> Unit) =
        viewModelScope.launch ( Dispatchers.IO ) {
            REPOSITORY.deleteNote(note) {
                onSuccess()
            }
        }

    fun replaceNote(oldNote: NoteModel, newNote : NoteModel, onSuccess:()-> Unit) =
        viewModelScope.launch ( Dispatchers.IO ) {
            REPOSITORY.deleteNote(oldNote) {
                onSuccess()
            }
            REPOSITORY.insertNote(newNote){
                onSuccess()
            }
        }

    fun getDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getCurrentDateTimeUsingJavaTime()
        } else {
            getCurrentDateTimeUsingCalendar()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTimeUsingJavaTime() : String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val formatted = currentDateTime.format(formatter)
        Log.d("date","Current Date and Time: $formatted")
        return formatted
    }

    fun getCurrentDateTimeUsingCalendar() : String{
        val currentDateTime = Calendar.getInstance().time
        Log.d("date","Current Date and Time: $currentDateTime")
        return currentDateTime.toString()
    }
}