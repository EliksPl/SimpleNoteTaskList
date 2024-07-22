package com.simpleNotes.simplenotelist.screens.tasksFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.simpleNotes.simplenotelist.MAIN
import com.simpleNotes.simplenotelist.REPOSITORY
import com.simpleNotes.simplenotelist.alarm.shedulers.AndroidAlarmScheduler
import com.simpleNotes.simplenotelist.model.dbModel.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class TasksViewModel(application: Application): AndroidViewModel(application) {
    val context = application

    fun allTasks() : Flow<List<TaskModel>> = REPOSITORY.getAllTasks()

    fun deleteSelectedTasks(selectedTasks: ArrayList<TaskModel>){
        viewModelScope.launch ( Dispatchers.IO ) {
            for (task in selectedTasks) {
                if(task.remindOnDate > 0){
                    val alarmScheduler = AndroidAlarmScheduler(MAIN)
//                    val taskAlarmItem = TaskAlarmItem(
//                        time = task.remindOnDate,
//                        taskName = task.title,
//                        priority = task.priority
//                    )
                    task.let (alarmScheduler::cancel)
                }

                REPOSITORY.deleteTask(task)
            }
        }
    }
}