package com.simpleNotes.simplenotelist.screens.addTaskFragment

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simpleNotes.simplenotelist.CHANNEL_ID
import com.simpleNotes.simplenotelist.MAIN
import com.simpleNotes.simplenotelist.REPOSITORY
import com.simpleNotes.simplenotelist.alarm.shedulers.AndroidAlarmScheduler
import com.simpleNotes.simplenotelist.model.dbModel.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTaskViewModel : ViewModel() {
    fun tryInsert(task: TaskModel) =
        viewModelScope.launch ( Dispatchers.IO ) {
            if (task.title != "") {
                REPOSITORY.insertTask(task)

                if(task.remindOnDate > 0){
                    val alarmScheduler = AndroidAlarmScheduler(MAIN)

                    task.let (alarmScheduler::schedule)
                }
            }
        }

    fun createNotificationChannel(){
        viewModelScope.launch(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Task alarms"
                val desc = "Notifications for task's alarms! "
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, name, importance)
//            val alarmUri = Settings.System.DEFAULT_ALARM_ALERT_URI
//            val attribute = AudioAttributes.Builder()
//                .setUsage(AudioAttributes.USAGE_ALARM)
//                .build()

                channel.description = desc
                channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//            channel.setSound(alarmUri, attribute)
                channel.enableVibration(true)

                val notificationManager =
                    MAIN.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }


}