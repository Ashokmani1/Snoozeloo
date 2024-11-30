package com.sample.snoozeloo.viewmodel

import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.snoozeloo.model.Alarm
import com.sample.snoozeloo.model.RingtoneItem
import com.sample.snoozeloo.repository.AlarmRepository
import com.sample.snoozeloo.util.AlarmManagerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class AlarmDetailViewModel @Inject constructor(private val repository: AlarmRepository) : ViewModel() {

    private val _hour = MutableStateFlow("")
    val hour: StateFlow<String> = _hour

    private val _minute = MutableStateFlow("")
    val minute: StateFlow<String> = _minute

    private val _alarmName = MutableStateFlow("")
    val alarmName: StateFlow<String> = _alarmName

    private val _repeatDays = MutableStateFlow(setOf<DayOfWeek>())
    val repeatDays: StateFlow<Set<DayOfWeek>> = _repeatDays

    private val _ringtone = MutableStateFlow(RingtoneItem())
    val ringtone: StateFlow<RingtoneItem?> = _ringtone

    private val _vibrate = MutableStateFlow(true)
    val vibrate: StateFlow<Boolean> = _vibrate

    private val _isTimeValid = MutableStateFlow(false)
    val isTimeValid: StateFlow<Boolean> = _isTimeValid

    private val _volume = MutableStateFlow(50F)
    val volume: StateFlow<Float> = _volume


    fun setHour(hour: String) {
        viewModelScope.launch {
            _hour.value = hour
            validateTime()
        }
    }

    fun setMinute(minute: String) {
        viewModelScope.launch {
            _minute.value = minute
            validateTime()
        }
    }

    fun setAlarmName(name: String) {
        viewModelScope.launch {
            _alarmName.value = name
        }
    }

    fun setRepeatDays(days: Set<DayOfWeek>) {
        viewModelScope.launch {
            _repeatDays.value = days
        }
    }

    fun setRingtone(ringtone: RingtoneItem?) {

        viewModelScope.launch {

            if (ringtone != null)
            {
                _ringtone.value = ringtone
            }
        }
    }

    fun setVibrate(vibrate: Boolean) {
        viewModelScope.launch {
            _vibrate.value = vibrate
        }
    }

    fun setVolume(volume: Float) {
        viewModelScope.launch {
            _volume.value = volume
        }
    }

    private fun validateTime()
    {
        val hour = _hour.value.toIntOrNull()
        val minute = _minute.value.toIntOrNull()
        _isTimeValid.value = hour in 0..23 && minute in 0..59
    }


    fun getAlarm(alarmId: Int, onAlarmFetched: () -> Unit) {

        viewModelScope.launch {

            repository.getAlarmById(alarmId).collect { alarm ->
                alarm?.let {
                    _hour.value = it.hour.toString()
                    _minute.value = it.minute.toString()
                    _alarmName.value = it.name
                    _repeatDays.value = it.repeatDays
                    _ringtone.value = it.ringtone
                    _vibrate.value = it.vibrate
                    _volume.value = it.volume
                }

                validateTime()
                onAlarmFetched()
            }
        }
    }

    private fun checkAndUpdateRingtone(context: Context) {

        viewModelScope.launch {

            if (ringtone.value != null) {

                if (ringtone.value?.title == "Default")
                {
                    val ringtoneManager = RingtoneManager(context)

                    ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE)

                    val cursor = ringtoneManager.cursor

                    if (cursor.moveToFirst())
                    {
                        val uri = ringtoneManager.getRingtoneUri(cursor.position)

                        _ringtone.value = RingtoneItem(title = "Default", uri = uri)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveOrUpdateAlarm(context: Context, alarmId: Int) {

        checkAndUpdateRingtone(context)

        viewModelScope.launch {

            val alarm = Alarm(
                id = if (alarmId == 0) 0 else alarmId,
                name = _alarmName.value.ifEmpty {  "Work" },
                hour = _hour.value.toInt(),
                minute = _minute.value.toInt(),
                repeatDays = _repeatDays.value,
                ringtone = _ringtone.value,
                vibrate = _vibrate.value,
                isEnabled = true,
                volume = _volume.value
            )

            if (alarmId == 0)
            {
                repository.insert(alarm)
            }
            else
            {
                repository.update(alarm)
                // if the alarm is already set, cancel it and set it again
                AlarmManagerUtil.cancelAlarm(context, alarm)
            }

            AlarmManagerUtil.setAlarm(context, alarm)
        }
    }
}