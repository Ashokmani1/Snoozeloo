package com.sample.snoozeloo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.sample.snoozeloo.components.CommonSpacer
import com.sample.snoozeloo.model.Alarm
import com.sample.snoozeloo.ui.theme.DIMEN_10_dp
import com.sample.snoozeloo.ui.theme.DIMEN_11_dp
import com.sample.snoozeloo.ui.theme.DIMEN_15_dp
import com.sample.snoozeloo.ui.theme.DIMEN_24_SP
import com.sample.snoozeloo.ui.theme.DIMEN_5_dp
import com.sample.snoozeloo.ui.theme.DIME_3_dp
import com.sample.snoozeloo.ui.theme.DIME_6_dp
import com.sample.snoozeloo.ui.theme.DIME_7_dp
import com.sample.snoozeloo.ui.theme.SnoozelooTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmListScreen(navController: NavController) {

    Scaffold(

        topBar = {

            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(stringResource(R.string.sz_your_alarms), style = MaterialTheme.typography.titleSmall)
                }
            )
        },

        floatingActionButton = {

            FloatingActionButton(shape = CircleShape, onClick = { navController.navigate("alarm_detail") }) {

                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.sz_add_alarm))
            }

        },

        floatingActionButtonPosition = FabPosition.Center,
    ) {

        val alarms = remember { mutableStateListOf<Alarm>() } // replace with actual data source

        alarms.add(Alarm(id = 1, name = "Wake Up"))

        if (alarms.isEmpty())
        {
            Column(modifier = Modifier.fillMaxSize().padding(DIMEN_15_dp),
                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                Image(painterResource(R.drawable.sz_alarm_blue_icon), contentDescription = null)

                Spacer(Modifier.height(DIMEN_10_dp))

                Text(stringResource(R.string.sz_alarm_list_empty_msg), textAlign = TextAlign.Center)
            }
        }
        else
        {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(it).padding(horizontal = DIMEN_15_dp)) {

                items(alarms) { alarm ->

                    AlarmItem(alarm)
                }
            }
        }
    }
}


@Composable
fun AlarmItem(alarm: Alarm) {

    var checked by remember { mutableStateOf(true) }

    Card(shape = RoundedCornerShape(DIMEN_10_dp))  {

        Column(Modifier.padding(vertical = DIMEN_10_dp, horizontal = DIMEN_15_dp)) {

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                Text(text = alarm.name, modifier = Modifier.weight(1f).padding(end = DIMEN_10_dp), style = MaterialTheme.typography.headlineSmall)

                Switch(checked = checked, onCheckedChange = { checked = it })
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {

                Text(text = "10:00", style = MaterialTheme.typography.titleLarge.copy())

                Text(text = "AM", style = MaterialTheme.typography.bodyLarge.copy(fontSize = DIMEN_24_SP), modifier = Modifier.padding(start = DIME_3_dp, bottom = DIME_6_dp))
            }


            Text(text = "Alarm in 30min", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)

            CommonSpacer()

            SuggestionChipLayout()

            CommonSpacer(DIME_7_dp)

            Text(text = "Go to bed at 02:00AM to get 8h of sleep", modifier = Modifier.padding(bottom = DIME_7_dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SuggestionChipLayout()
{

    val chips by remember { mutableStateOf(listOf("Mo", "Tu", "We","Th","Fr","Sa", "Su")) }

    var chipState by remember { mutableStateOf(listOf<String>()) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        FlowRow(modifier = Modifier.fillMaxWidth()) {

            chips.forEach {

                SuggestionChipEachRow(chip = it, chipState.contains(it)) { chip ->

                    chipState = if (chipState.contains(chip)) chipState.minus(chip) else chipState.plus(chip)
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
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun AlarmPreview()
{
    SnoozelooTheme {

        AlarmItem(Alarm(id = 1, name = "Wake up"))
    }
}