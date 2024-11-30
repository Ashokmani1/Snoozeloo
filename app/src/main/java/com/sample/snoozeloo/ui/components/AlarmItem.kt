package com.sample.snoozeloo.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.format.DateTimeFormatter
import com.sample.snoozeloo.model.Alarm
import com.sample.snoozeloo.ui.theme.DIMEN_10_dp
import com.sample.snoozeloo.ui.theme.DIMEN_15_dp
import com.sample.snoozeloo.ui.theme.DIMEN_24_SP
import com.sample.snoozeloo.ui.theme.DIME_3_dp
import com.sample.snoozeloo.ui.theme.DIME_6_dp
import com.sample.snoozeloo.ui.theme.DIME_7_dp
import com.sample.snoozeloo.viewmodel.AlarmViewModel
import java.time.Duration
import java.time.LocalTime
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlarmItem(alarm: Alarm, viewModel: AlarmViewModel, onClick: () -> Unit) {

    var checked by remember { mutableStateOf(alarm.isEnabled) }

    var nextOccurrence by remember { mutableStateOf(viewModel.calculateNextOccurrence(alarm)) }

    val currentTime = LocalTime.now()

    val alarmTime = LocalTime.of(alarm.hour, alarm.minute)

    val durationUntilAlarm = Duration.between(currentTime, alarmTime)

    val showBedTime = alarmTime.isAfter(LocalTime.of(4, 0)) && alarmTime.isBefore(LocalTime.of(10, 0)) &&
            durationUntilAlarm.toHours() >= 8

    val bedTime = alarmTime.minusHours(8)

    Card(shape = RoundedCornerShape(DIMEN_10_dp), onClick = { onClick() }, modifier = Modifier.padding(vertical = DIMEN_10_dp)) {

        Column(Modifier.padding(vertical = DIMEN_10_dp, horizontal = DIMEN_15_dp)) {

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = alarm.name,
                    modifier = Modifier.weight(1f).padding(end = DIMEN_10_dp),
                    style = MaterialTheme.typography.headlineSmall
                )

                Switch(checked = checked, onCheckedChange = {

                    checked = it
                    viewModel.updateAlarmEnabled(alarm.id, it)})
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                Text(
                    text = String.format(Locale.US, "%02d:%02d", alarm.hour, alarm.minute),
                    style = MaterialTheme.typography.titleLarge.copy()
                )
                Text(
                    text = if (alarm.hour < 12) "AM" else "PM",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = DIMEN_24_SP),
                    modifier = Modifier.padding(start = DIME_3_dp, bottom = DIME_6_dp)
                )
            }

            if (checked && alarm.repeatDays.isNotEmpty())
            {
                Text(
                    text = "Next occurrence: $nextOccurrence",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            CommonVerticalSpacer()

            SuggestionChipLayout(alarm.repeatDays) { updatedDays ->

                viewModel.updateAlarmRepeatDays(alarm.id, updatedDays)
                nextOccurrence = viewModel.calculateNextOccurrence(alarm.copy(repeatDays = updatedDays))
            }

            if (showBedTime) {

                CommonVerticalSpacer()

                Text(
                    text = "Go to bed at ${bedTime.format(DateTimeFormatter.ofPattern("hh:mm a"))} to get 8h of sleep",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            CommonVerticalSpacer(DIME_7_dp)

        }
    }
}