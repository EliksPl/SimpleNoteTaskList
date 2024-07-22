package com.simpleNotes.simplenotelist.model.dbModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.simpleNotes.simplenotelist.NOTE_DB_TABLE_NAME
import java.io.Serializable

@Entity(tableName = NOTE_DB_TABLE_NAME)
class NoteModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo var title: String = "",
    @ColumnInfo var desc: String = "",
    @ColumnInfo(name = "date", defaultValue = "") var date: String = ""
) : Serializable