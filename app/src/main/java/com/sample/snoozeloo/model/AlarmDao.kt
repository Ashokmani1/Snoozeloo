package com.sample.snoozeloo.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek

@Dao
interface AlarmDao
{
    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): Flow<List<Alarm>>

    @Insert
    suspend fun insert(alarm: Alarm)

    @Update
    suspend fun update(alarm: Alarm)

    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("UPDATE alarms SET isEnabled = :isEnabled WHERE id = :alarmId")
    suspend fun updateAlarmEnabled(alarmId: Int, isEnabled: Boolean)

    @Query("UPDATE alarms SET repeatDays = :repeatDays WHERE id = :alarmId")
    suspend fun updateAlarmRepeatDays(alarmId: Int, repeatDays: String)

    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    fun getAlarmById(alarmId: Int): Flow<Alarm?>
}