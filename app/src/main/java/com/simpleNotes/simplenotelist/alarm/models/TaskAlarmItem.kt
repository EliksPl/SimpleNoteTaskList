package com.simpleNotes.simplenotelist.alarm.models

import android.os.Parcel
import android.os.Parcelable

class TaskAlarmItem(
    val time: Long = System.currentTimeMillis(),
    val taskName: String = "",
    val priority: Int = 0
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(time)
        parcel.writeString(taskName)
        parcel.writeInt(priority)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskAlarmItem> {
        override fun createFromParcel(parcel: Parcel): TaskAlarmItem {
            return TaskAlarmItem(parcel)
        }

        override fun newArray(size: Int): Array<TaskAlarmItem?> {
            return arrayOfNulls(size)
        }
    }
}