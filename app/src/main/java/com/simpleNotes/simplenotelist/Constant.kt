package com.simpleNotes.simplenotelist

import com.simpleNotes.simplenotelist.db.repository.NoteRepository

lateinit var MAIN : MainActivity
lateinit var REPOSITORY: NoteRepository //Room repository (init in NotesViewModel)
const val BUNDLE_KEY_NOTE_TO_DESC = "note" //Bundle key for NotesFragment -> NoteDescriptionFragment navigation
const val BUNDLE_KEY_TASK_TO_DESC = "task" //Bundle key for TasksFragment -> TaskDescriptionFragment navigation
//const val REQUEST_KEY_DATE_TIME_PICKER = "date_time_picker"
//const val BUNDLE_KEY_DATE_TIME_PICKER = "date_time_picker_bundle"

const val NOTIFICATION_ID = 1
const val CHANNEL_ID = "task_notification_channel"

const val ALARM_MESSAGE_KEY = "EXTRA_ALARM_MESSAGE"
const val ALARM_ACTIVITY_ITEM_KEY = "extra_activity_alarm_TaskAlarmItem"

const val NOTE_DB_TABLE_NAME = "note_table"
const val TASK_DB_TABLE_NAME = "task_table"