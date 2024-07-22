package com.simpleNotes.simplenotelist.model.dbModel
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.simpleNotes.simplenotelist.TASK_DB_TABLE_NAME
import java.io.Serializable

@Entity(tableName = TASK_DB_TABLE_NAME)
data class TaskModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo var title: String = "",
    @ColumnInfo var priority: Int = 0,
    @ColumnInfo(name = "remind_on_date")var remindOnDate: Long
): Serializable
