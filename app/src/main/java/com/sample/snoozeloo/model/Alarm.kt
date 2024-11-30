package com.sample.snoozeloo.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val hour: Int,
    val minute: Int,
    val repeatDays: Set<DayOfWeek>,
    val ringtone: RingtoneItem,
    val vibrate: Boolean,
    val isEnabled: Boolean,
    val volume: Float
): Parcelable
{
    constructor(parcel: Parcel) : this(
    parcel.readInt(),
    parcel.readString() ?: "",
    parcel.readInt(),
    parcel.readInt(),
    parcel.createStringArray()?.map { DayOfWeek.valueOf(it) }?.toSet() ?: emptySet(),
    parcel.readParcelable(RingtoneItem::class.java.classLoader) ?: RingtoneItem(),
    parcel.readByte() != 0.toByte(),
    parcel.readByte() != 0.toByte(),
    parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(hour)
        parcel.writeInt(minute)
        parcel.writeStringArray(repeatDays.map { it.name }.toTypedArray())
        parcel.writeParcelable(ringtone, flags)
        parcel.writeByte(if (vibrate) 1 else 0)
        parcel.writeByte(if (isEnabled) 1 else 0)
        parcel.writeFloat(volume)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Alarm> {
        override fun createFromParcel(parcel: Parcel): Alarm {
            return Alarm(parcel)
        }

        override fun newArray(size: Int): Array<Alarm?> {
            return arrayOfNulls(size)
        }
    }
}