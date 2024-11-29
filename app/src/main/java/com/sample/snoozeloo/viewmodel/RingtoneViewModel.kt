package com.sample.snoozeloo.viewmodel

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.snoozeloo.model.RingtoneItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RingtoneViewModel : ViewModel() {

    private val _ringtones = MutableStateFlow<List<RingtoneItem>>(emptyList())
    val ringtones: StateFlow<List<RingtoneItem>> = _ringtones

    private val _currentRingtone = MutableStateFlow<Ringtone?>(null)

    private val _selectedRingtone = MutableStateFlow<RingtoneItem?>(null)
    val selectedRingtone: StateFlow<RingtoneItem?> = _selectedRingtone

    fun fetchRingtones(context: Context)
    {
        viewModelScope.launch {

            val ringtoneManager = RingtoneManager(context)
            ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE)

            val cursor = ringtoneManager.cursor

            val ringtoneList = mutableListOf<RingtoneItem>().apply {
                add(RingtoneItem("Silent", null))
            }

            while (cursor.moveToNext())
            {
                val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
                val uri = ringtoneManager.getRingtoneUri(cursor.position)
                ringtoneList.add(RingtoneItem(title = title, uri = uri))
            }

            _ringtones.value = ringtoneList
        }
    }

    fun setSelectedRingtone(ringtone: RingtoneItem?)
    {
        _selectedRingtone.value = ringtone
    }

    fun stopCurrentRingtone()
    {
        _currentRingtone.value?.stop()
    }

    fun selectRingtone(context: Context, ringtoneItem: RingtoneItem)
    {
        _currentRingtone.value?.stop()

        if (ringtoneItem.uri != null)
        {
            val newRingtone = RingtoneManager.getRingtone(context, ringtoneItem.uri)
            _currentRingtone.value = newRingtone
            newRingtone.play()
            _selectedRingtone.value = ringtoneItem
        }
        else
        {
            _currentRingtone.value = null
            _selectedRingtone.value = null
        }
    }
}