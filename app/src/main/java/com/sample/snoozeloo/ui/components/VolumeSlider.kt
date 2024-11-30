package com.sample.snoozeloo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import com.sample.snoozeloo.viewmodel.AlarmDetailViewModel

@Composable
fun VolumeSlider(viewModel: AlarmDetailViewModel) {

    val sliderPosition by viewModel.volume.collectAsState()

    Column {
        Slider(
            value = sliderPosition,
            onValueChange = {
                viewModel.setVolume(it)
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            valueRange = 0f..100f
        )
    }
}