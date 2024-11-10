package com.sample.snoozeloo.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
data class Alarm (
    val id: Int,
    val time: LocalTime = LocalTime.MIDNIGHT,
    val repeatDays: Set<DayOfWeek> = setOf(),
    var isEnabled: Boolean = true,
    val name: String = "",
    val ringtone: String = "default",
    val volume: Int = 50,
    val vibrate: Boolean = true
)
