package com.simpleNotes.simplenotelist.alarm.shedulers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
import com.simpleNotes.simplenotelist.ALARM_MESSAGE_KEY
import com.simpleNotes.simplenotelist.MAIN
import com.simpleNotes.simplenotelist.alarm.models.TaskAlarmItem
import com.simpleNotes.simplenotelist.alarm.receivers.AlarmReceiver
import com.simpleNotes.simplenotelist.model.dbModel.TaskModel
import java.time.LocalDateTime
import java.time.ZoneId

class AndroidAlarmScheduler(
    private val context: Context
): AlarmScheduler {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(item: TaskModel) {
        if (item.remindOnDate < LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()){
            return
        }

        val taskAlarmItem = TaskAlarmItem(
            time = item.remindOnDate,
            taskName = item.title,
            priority = item.priority
        )

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(ALARM_MESSAGE_KEY,taskAlarmItem)
        }
//


//        val alarmClockInfo = AlarmManager.AlarmClockInfo(
//            item.time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
//            getAlarmInfoPendingIntent(item)
//        )

//        alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent(item))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            when {
                // If permission is granted, proceed with scheduling exact alarms.
                alarmManager.canScheduleExactAlarms() -> {
                    setExactAlarm(item, intent)
                }

                !(alarmManager.canScheduleExactAlarms()) -> {
                    // Ask users to go to exact alarm page in system settings.
                    MAIN.startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                }
            }
        }else{
            setExactAlarm(item, intent)
        }


    }

    @SuppressLint("MissingPermission")
    private fun setExactAlarm(item: TaskModel, intent: Intent){
        val hash = item.title.hashCode()+item.priority.hashCode()+item.remindOnDate.hashCode()
        Log.d("alarm", hash.toString())
        val pendIntent = PendingIntent.getBroadcast(
            context,
            hash,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.remindOnDate,
            pendIntent
        )
    }

    override fun cancel(item: TaskModel) {
        val hash = item.title.hashCode()+item.priority.hashCode()+item.remindOnDate.hashCode()
        Log.d("alarm", hash.toString())
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendIntent = PendingIntent.getBroadcast(
            context,
            hash,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendIntent)
//        alarmManager.cancel(
//            PendingIntent.getBroadcast(
//                context,
//                item.hashCode(),
//                Intent(context, AlarmReceiver::class.java).apply {
//                    putExtra(ALARM_MESSAGE_KEY,item)
//                },
//                PendingIntent.FLAG_NO_CREATE
//            )
//        )
    }

//    private fun getAlarmInfoPendingIntent(item : TaskAlarmItem): PendingIntent{
//        val alarmInfoIntent = Intent(MAIN, MainActivity::class.java)
//        alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//        return PendingIntent.getActivity(MAIN, item.hashCode(), alarmInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//    }
//
//    private fun getAlarmActionPendingIntent(item : TaskAlarmItem):PendingIntent{
//        val intent = Intent(MAIN, OnAlarmActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//        return PendingIntent.getActivity(MAIN, item.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
//    }
}