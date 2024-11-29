package com.sample.snoozeloo.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sample.snoozeloo.model.typeConverter.Converters
import com.sample.snoozeloo.model.typeConverter.RingtoneItemTypeConverter
import com.sample.snoozeloo.model.typeConverter.UriTypeConverter

@Database(entities = [Alarm::class], version = 1)
@TypeConverters(Converters::class, UriTypeConverter::class, RingtoneItemTypeConverter::class)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}