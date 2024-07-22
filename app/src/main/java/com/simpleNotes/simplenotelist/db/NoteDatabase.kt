package com.simpleNotes.simplenotelist.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.simpleNotes.simplenotelist.db.dao.NoteDao
import com.simpleNotes.simplenotelist.db.dao.TaskDao
import com.simpleNotes.simplenotelist.model.dbModel.NoteModel
import com.simpleNotes.simplenotelist.model.dbModel.TaskModel

@Database(
    entities = [NoteModel::class, TaskModel::class],
    version = 1
)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun getNoteDao():NoteDao
    abstract fun getTaskDao():TaskDao

    companion object{
        private var database: NoteDatabase ?= null

        @Synchronized
        fun getInstance(context: Context):NoteDatabase{
            return if(database == null){
                database = Room.databaseBuilder(context, NoteDatabase::class.java, "testdb.db").build()

                database as NoteDatabase
            }else{
                database as NoteDatabase
            }
        }
    }
}