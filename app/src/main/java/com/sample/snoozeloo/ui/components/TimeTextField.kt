package com.sample.snoozeloo.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sample.snoozeloo.viewmodel.AlarmDetailViewModel

@Composable
fun TimeTextField(isHour: Boolean, error: MutableState<Boolean>, errorMessage: MutableState<String>, viewModel: AlarmDetailViewModel) {

    val text by if (isHour) viewModel.hour.collectAsState() else viewModel.minute.collectAsState()

    OutlinedTextField(
        value = text,
        maxLines = 1,
        onValueChange = {
            if (it.all { char -> char.isDigit() }) {
                if (isHour) {
                    if (it.length <= 2) {
                        val hour = it.toIntOrNull() ?: 0
                        if (hour in 0..23) {
                            error.value = false
                            errorMessage.value = ""
                        } else {
                            error.value = true
                            errorMessage.value = "Hours must be between 00-23."
                        }
                        viewModel.setHour(it)
                    }
                }
                else {
                    if (it.length <= 2) {

                        val minute = it.toIntOrNull() ?: 0
                        if (minute in 0..59) {
                            error.value = false
                            errorMessage.value = ""
                        } else {
                            error.value = true
                            errorMessage.value = "Minutes must be between 00-59."
                        }
                        viewModel.setMinute(it)
                    }
                }
            }
        },
        shape = MaterialTheme.shapes.large,
        placeholder = {
            Text(
                text = "00",
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 52.sp)
            )
        },
        modifier = Modifier.fillMaxWidth().heightIn(min = 95.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.titleLarge.copy(fontSize = 52.sp, textAlign = TextAlign.Center),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}
