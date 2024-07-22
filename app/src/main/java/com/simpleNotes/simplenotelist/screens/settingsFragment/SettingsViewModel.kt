package com.simpleNotes.simplenotelist.screens.settingsFragment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simpleNotes.simplenotelist.REPOSITORY
import com.simpleNotes.simplenotelist.model.dbModel.NoteModel
import com.simpleNotes.simplenotelist.model.dbModel.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    fun deleteAllNotes(function: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.deleteAllNotes {
                function()
            }
            REPOSITORY.deleteAllTasks()
        }
    }

    fun addNotes(array: ArrayList<NoteModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (note in array) {
                REPOSITORY.insertNote(note) {

                }
            }
        }
    }

    fun addTasks(array: ArrayList<TaskModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (task in array) {
                REPOSITORY.insertTask(task)
            }
        }
    }

    fun getAllNotes() : List<NoteModel> = REPOSITORY.getAllNotesOnce()// =
//        viewModelScope.launch(Dispatchers.IO) {
//        REPOSITORY.getAllNotes()
//    }

    fun getAllTasks() : List<TaskModel> = REPOSITORY.getAllTasksOnce()//REPOSITORY.getAllTasks()
//        viewModelScope.launch(Dispatchers.IO) {
//
//    }


    fun saveFile(context: Context) {
//        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//
//        val folder = File(directory?.absolutePath!!)
//
//        if (!folder.exists()){
//            folder.mkdirs()
//        }
//
//        val file = File(directory,"backup.txt")
//
//        Log.d("fileSave", "saveFile: "+file.absolutePath)
////        file.mkdirs()


    }

    fun openFile(){

    }




}