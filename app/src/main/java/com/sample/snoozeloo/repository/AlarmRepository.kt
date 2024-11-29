package com.sample.snoozeloo.repository

import com.sample.snoozeloo.model.Alarm
import com.sample.snoozeloo.model.AlarmDao
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import javax.inject.Inject

class AlarmRepository @Inject constructor(private val alarmDao: AlarmDao)
{
    val allAlarms: Flow<List<Alarm>> = alarmDao.getAllAlarms()

    suspend fun insert(alarm: Alarm) {

        alarmDao.insert(alarm)
    }

    suspend fun update(alarm: Alarm) {

        alarmDao.update(alarm)
    }

    suspend fun delete(id: Int) {

        alarmDao.delete(id)
    }

    fun getAlarmById(alarmId: Int): Flow<Alarm?> = alarmDao.getAlarmById(alarmId)

    suspend fun updateAlarmEnabled(alarmId: Int, isEnabled: Boolean) {

        alarmDao.updateAlarmEnabled(alarmId, isEnabled)
    }

    suspend fun updateAlarmRepeatDays(alarmId: Int, repeatDays: Set<DayOfWeek>) {

        val repeatDaysString = repeatDays.joinToString(",") { it.name }

        alarmDao.updateAlarmRepeatDays(alarmId, repeatDaysString)
    }
}