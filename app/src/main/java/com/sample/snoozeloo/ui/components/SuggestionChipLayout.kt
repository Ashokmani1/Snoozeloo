package com.sample.snoozeloo.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sample.snoozeloo.model.Alarm
import com.sample.snoozeloo.ui.theme.DIMEN_11_dp
import com.sample.snoozeloo.ui.theme.DIMEN_5_dp
import com.sample.snoozeloo.ui.theme.DIME_6_dp
import com.sample.snoozeloo.ui.theme.DIME_7_dp
import java.time.DayOfWeek

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SuggestionChipLayout(alarm: Set<DayOfWeek>, onChipStateChange: (Set<DayOfWeek>) -> Unit)
{
    val chips = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")

    val dayOfWeekMap = mapOf(
        "Mo" to DayOfWeek.MONDAY,
        "Tu" to DayOfWeek.TUESDAY,
        "We" to DayOfWeek.WEDNESDAY,
        "Th" to DayOfWeek.THURSDAY,
        "Fr" to DayOfWeek.FRIDAY,
        "Sa" to DayOfWeek.SATURDAY,
        "Su" to DayOfWeek.SUNDAY
    )

    var chipState by remember { mutableStateOf(alarm.map { it.name.take(1).uppercase() + it.name.drop(1).lowercase().take(1) }.toSet()) }

    LaunchedEffect(alarm) {

        chipState = alarm.map { it.name.take(1).uppercase() + it.name.drop(1).lowercase().take(1) }.toSet()
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FlowRow(modifier = Modifier.fillMaxWidth()) {

            chips.forEach { chip ->

                SuggestionChipEachRow(chip = chip, selected = chipState.contains(chip)) { selectedChip ->

                    chipState = if (chipState.contains(selectedChip)) chipState.minus(selectedChip) else chipState.plus(selectedChip)

                    val updatedDays = chipState.mapNotNull { dayOfWeekMap[it] }.toSet()
                    onChipStateChange(updatedDays)
                }
            }
        }
    }
}

@Composable
fun SuggestionChipEachRow(chip: String, selected: Boolean, onChipState: (String) -> Unit) {

    Box(modifier = Modifier.padding(end = DIME_7_dp, bottom = DIME_6_dp)) {

        Surface(
            shape = MaterialTheme.shapes.large,
            color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.secondary)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onChipState(chip) }) {

                Text(
                    chip,
                    modifier = Modifier.padding(horizontal = DIMEN_11_dp, vertical = DIMEN_5_dp),
                    style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}