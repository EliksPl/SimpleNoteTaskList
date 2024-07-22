package com.simpleNotes.simplenotelist.alarm.shedulers

import com.simpleNotes.simplenotelist.model.dbModel.TaskModel

interface AlarmScheduler {
    fun schedule(item: TaskModel)
    fun cancel(item: TaskModel)
}