package com.simpleNotes.simplenotelist.alarm

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.simpleNotes.simplenotelist.ALARM_ACTIVITY_ITEM_KEY
import com.simpleNotes.simplenotelist.alarm.models.TaskAlarmItem
import com.simpleNotes.simplenotelist.alarm.receivers.AlarmReceiver
import com.simpleNotes.simplenotelist.databinding.ActivityOnAlarmBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class OnAlarmActivity: AppCompatActivity() {
    private lateinit var binding : ActivityOnAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("alarmActivity", "onCreate: here")
        binding = ActivityOnAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AlarmReceiver.player.stop()

        init()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val taskAlarmItem = getItemFromIntent(intent)
        Log.d("newIntent", "onNewIntent: ${taskAlarmItem.taskName}")

//        binding.activityOnAlarmTvDateTime.text =
//            LocalDateTime.ofInstant(Instant.ofEpochMilli(taskAlarmItem.time), ZoneId.systemDefault()).toString()
//
//        binding.activityOnAlarmTvTaskText.text = taskAlarmItem.taskName
    }

    private fun init(){
        val taskAlarmItem = getItemFromIntent(intent)
        Log.d("newIntent", "onNewIntent: ${taskAlarmItem.taskName}")
        val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)

        binding.activityOnAlarmTvDateTime.text =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(taskAlarmItem.time), ZoneId.systemDefault()).format(dateFormatter).toString()

        binding.activityOnAlarmTvTaskText.text = taskAlarmItem.taskName

        binding.activityOnAlarmBtnOk.setOnClickListener{
            finish()
        }
    }

    @Suppress("DEPRECATION")
    private fun getItemFromIntent(newIntent: Intent): TaskAlarmItem {
        val item: TaskAlarmItem

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            item = newIntent.getParcelableExtra(ALARM_ACTIVITY_ITEM_KEY, TaskAlarmItem::class.java)!!
        } else {
            item = newIntent.getParcelableExtra(ALARM_ACTIVITY_ITEM_KEY)!!
        }

        return item
    }

}