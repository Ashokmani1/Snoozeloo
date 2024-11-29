package com.sample.snoozeloo.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.room.TypeConverters
import java.io.Serializable

data class RingtoneItem(
    val title: String = "Default",
    val uri: Uri? = null
): Parcelable
{
    constructor(parcel: Parcel) : this(
    parcel.readString() ?: "Default",
        parcel.readParcelable(Uri::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeParcelable(uri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RingtoneItem> {

        override fun createFromParcel(parcel: Parcel): RingtoneItem {

            return RingtoneItem(parcel)
        }

        override fun newArray(size: Int): Array<RingtoneItem?> {

            return arrayOfNulls(size)
        }
    }
}