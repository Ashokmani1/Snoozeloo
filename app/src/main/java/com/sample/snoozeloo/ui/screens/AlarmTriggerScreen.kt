package com.sample.snoozeloo.ui.screens

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sample.snoozeloo.R
import com.sample.snoozeloo.util.AlarmManagerUtil
import com.sample.snoozeloo.model.Alarm
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlarmTriggerScreen(alarm: Alarm) {

    val context = LocalContext.current

    Scaffold(
        content = {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(painterResource(R.drawable.sz_alarm_blue_icon), contentDescription = null)

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = String.format(Locale.US, "%02d:%02d", alarm.hour, alarm.minute) + if (alarm.hour < 12) " AM" else " PM", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = alarm.name, style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { turnOffAlarm(context, alarm) }) {

                    Text("Turn Off")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { snoozeAlarm(context, alarm) }) {

                    Text("Snooze for 5 minutes")
                }
            }
        }
    )
}

private fun turnOffAlarm(context: Context, alarm: Alarm)
{
    AlarmManagerUtil.cancelAlarm(context, alarm)
    (context as? Activity)?.finish()
}

@RequiresApi(Build.VERSION_CODES.O)
private fun snoozeAlarm(context: Context, alarm: Alarm)
{
    val snoozeTimeInMinutes = 5

    val snoozeCalendar = Calendar.getInstance().apply {

        add(Calendar.MINUTE, snoozeTimeInMinutes)
    }

    AlarmManagerUtil.setAlarm(context, alarm.copy(hour = snoozeCalendar.get(Calendar.HOUR_OF_DAY), minute = snoozeCalendar.get(Calendar.MINUTE)))

    (context as? Activity)?.finish()
}