package com.simpleNotes.simplenotelist.screens.TaskDescriptionFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simpleNotes.simplenotelist.MAIN
import com.simpleNotes.simplenotelist.REPOSITORY
import com.simpleNotes.simplenotelist.alarm.shedulers.AndroidAlarmScheduler
import com.simpleNotes.simplenotelist.model.dbModel.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant

class TaskDescriptionViewModel : ViewModel() {
//    fun tryDeleteTask(task: TaskModel) =
//        viewModelScope.launch ( Dispatchers.IO ) {
//            REPOSITORY.deleteTask(task)
//
//        }

    fun replaceTask(oldTask: TaskModel, newTask : TaskModel) =
        viewModelScope.launch ( Dispatchers.IO ) {
            REPOSITORY.deleteTask(oldTask)
            if(oldTask.remindOnDate > 0){
                val alarmScheduler = AndroidAlarmScheduler(MAIN)
                oldTask.let(alarmScheduler::cancel)
            }
            REPOSITORY.insertTask(newTask)
            if(newTask.remindOnDate > Instant.now().toEpochMilli()){
                val alarmScheduler = AndroidAlarmScheduler(MAIN)
                newTask.let(alarmScheduler::schedule)
            }
        }
}