package com.sample.snoozeloo.model.typeConverter

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.sample.snoozeloo.model.RingtoneItem

class RingtoneItemTypeConverter {

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Uri::class.java, UriTypeAdapter())
        .create()

    @TypeConverter
    fun fromRingtoneItem(ringtoneItem: RingtoneItem?): String? {

        return gson.toJson(ringtoneItem)
    }

    @TypeConverter
    fun toRingtoneItem(ringtoneItemString: String?): RingtoneItem? {

        return gson.fromJson(ringtoneItemString, object : TypeToken<RingtoneItem>() {}.type)
    }
}