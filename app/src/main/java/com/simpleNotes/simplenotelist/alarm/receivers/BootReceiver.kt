package com.simpleNotes.simplenotelist.alarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.simpleNotes.simplenotelist.REPOSITORY
import com.simpleNotes.simplenotelist.alarm.shedulers.AndroidAlarmScheduler
import com.simpleNotes.simplenotelist.db.NoteDatabase
import com.simpleNotes.simplenotelist.db.repository.NoteRealisation
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) = goAsync{
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
                initDatabase(context)
                val data = REPOSITORY.getAllReminders()
                for (task in data) {
                    if (task.remindOnDate > 0) {
                        val alarmScheduler = AndroidAlarmScheduler(context)
//                        val taskAlarmItem = TaskAlarmItem(
//                            time = task.remindOnDate,
//                            taskName = task.title,
//                            priority = task.priority
//                        )
                        task.let(alarmScheduler::schedule)
                    }
                }

        }
    }

    private fun initDatabase(context: Context){
        val daoNote = NoteDatabase.getInstance(context).getNoteDao()
        val daoTask = NoteDatabase.getInstance(context).getTaskDao()
        REPOSITORY = NoteRealisation(daoNote, daoTask)

    }

    private fun BroadcastReceiver.goAsync(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val pendingResult = goAsync()
        @OptIn(DelicateCoroutinesApi::class) // Must run globally; there's no teardown callback.
        GlobalScope.launch(context) {
            try {
                block()
            } finally {
                pendingResult.finish()
            }
        }
    }
}