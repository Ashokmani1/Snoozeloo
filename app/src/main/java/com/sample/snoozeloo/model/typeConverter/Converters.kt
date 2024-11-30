package com.sample.snoozeloo.model.typeConverter

import androidx.room.TypeConverter
import java.time.DayOfWeek

class Converters {

    @TypeConverter
    fun fromDayOfWeekSet(days: Set<DayOfWeek>): String {

        return days.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toDayOfWeekSet(days: String): Set<DayOfWeek> {
        return if (days.isEmpty()) {
            emptySet()
        } else {
            days.split(",").map { DayOfWeek.valueOf(it) }.toSet()
        }
    }
}