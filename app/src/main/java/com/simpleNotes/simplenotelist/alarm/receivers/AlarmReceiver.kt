package com.simpleNotes.simplenotelist.alarm.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.simpleNotes.simplenotelist.*
import com.simpleNotes.simplenotelist.alarm.OnAlarmActivity
import com.simpleNotes.simplenotelist.alarm.models.TaskAlarmItem

class AlarmReceiver: BroadcastReceiver(){
    companion object {
        lateinit var player: Ringtone
    }

    override fun onReceive(context: Context, intent: Intent) {

        val item = getItemFromIntent(intent)
        val activityIntent = Intent(context, OnAlarmActivity::class.java)
        activityIntent.putExtra(ALARM_ACTIVITY_ITEM_KEY, item)
        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val notificationPendingIntent = PendingIntent.getActivity(context, item.hashCode(), activityIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification  = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Alarm!")
            .setContentText(item.taskName)
            .setAutoCancel(true)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(notificationPendingIntent)
            .addAction(0,"Show",notificationPendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

//        notification.flags = NotificationCompat.FLAG_INSISTENT

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        player = RingtoneManager.getRingtone(context, soundUri)
        player.play()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)


    }

    @Suppress("DEPRECATION")
    private fun getItemFromIntent(funIntent: Intent): TaskAlarmItem {
        val item: TaskAlarmItem

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            item = funIntent.getParcelableExtra(ALARM_MESSAGE_KEY, TaskAlarmItem::class.java)!!
        } else {
            item = funIntent.getParcelableExtra(ALARM_MESSAGE_KEY)!!
        }

        return item
    }

}