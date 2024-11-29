package com.sample.snoozeloo.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.snoozeloo.model.Alarm
import com.sample.snoozeloo.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(private val repository: AlarmRepository) : ViewModel() {

    private val _alarms = MutableStateFlow<List<Alarm>>(emptyList())
    val alarms: StateFlow<List<Alarm>> = _alarms

    init {
        viewModelScope.launch {
            repository.allAlarms.collect {
                _alarms.value = it
            }
        }
    }


    fun update(alarm: Alarm) {
        viewModelScope.launch {
            repository.update(alarm)
        }
    }

    fun deleteAlarm(id: Int) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    fun updateAlarmEnabled(alarmId: Int, isEnabled: Boolean) {
        viewModelScope.launch {
            repository.updateAlarmEnabled(alarmId, isEnabled)
        }
    }

    fun updateAlarmRepeatDays(alarmId: Int, repeatDays: Set<DayOfWeek>) {
        viewModelScope.launch {
            repository.updateAlarmRepeatDays(alarmId, repeatDays)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateNextOccurrence(alarm: Alarm): String
    {
        val currentTime = LocalTime.now()
        val alarmTime = LocalTime.of(alarm.hour, alarm.minute)
        val today = LocalDate.now()
        val currentDayOfWeek = today.dayOfWeek

        val nextDay = alarm.repeatDays
            .map { dayOfWeek ->
                val daysUntilNext = (dayOfWeek.value - currentDayOfWeek.value + 7) % 7
                if (daysUntilNext == 0 && alarmTime.isAfter(currentTime)) {
                    today
                } else if (daysUntilNext == 0) {
                    today.plusDays(7)
                } else {
                    today.plusDays(daysUntilNext.toLong())
                }
            }
            .minByOrNull { it.toEpochDay() } ?: today

        val nextOccurrenceDateTime = LocalDateTime.of(nextDay, alarmTime)
        val durationUntilNextOccurrence = Duration.between(LocalDateTime.now(), nextOccurrenceDateTime)

        val days = durationUntilNextOccurrence.toDays()
        val totalHours = durationUntilNextOccurrence.toHours()
        val hours = totalHours % 24
        val totalMinutes = durationUntilNextOccurrence.toMinutes()
        val minutes = totalMinutes % 60

        return "${days}d ${hours}h ${minutes}min"
    }
}