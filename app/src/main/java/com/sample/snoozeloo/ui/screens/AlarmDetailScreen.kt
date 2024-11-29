package com.sample.snoozeloo.ui.screens

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sample.snoozeloo.R
import com.sample.snoozeloo.model.RingtoneItem
import com.sample.snoozeloo.model.typeConverter.RingtoneItemTypeConverter
import com.sample.snoozeloo.ui.components.CommonVerticalSpacer
import com.sample.snoozeloo.ui.components.CustomAlertDialog
import com.sample.snoozeloo.ui.components.SuggestionChipLayout
import com.sample.snoozeloo.ui.components.TimeTextField
import com.sample.snoozeloo.ui.components.VolumeSlider
import com.sample.snoozeloo.ui.theme.DIMEN_10_dp
import com.sample.snoozeloo.ui.theme.DIMEN_13_dp
import com.sample.snoozeloo.ui.theme.DIMEN_15_dp
import com.sample.snoozeloo.ui.theme.DIMEN_5_dp
import com.sample.snoozeloo.ui.theme.SnoozelooTheme
import com.sample.snoozeloo.viewmodel.AlarmDetailViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDetailScreen(alarmId: Int, navController: NavController, viewModel: AlarmDetailViewModel = hiltViewModel())
{
    val scrollState = rememberScrollState()

    LaunchedEffect(alarmId) {

        viewModel.getAlarm(alarmId) {

            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

            val selectedRingtone = savedStateHandle?.getStateFlow<RingtoneItem?>("selectedRingtone", null)?.value
            viewModel.setRingtone(selectedRingtone)
        }
    }

    val alarmName by viewModel.alarmName.collectAsState()

    val repeatDays by viewModel.repeatDays.collectAsState()

    val ringtone by viewModel.ringtone.collectAsState()

    val vibrate by viewModel.vibrate.collectAsState()

    val isTimeValid by viewModel.isTimeValid.collectAsState()

    val shouldShowDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (shouldShowDialog.value) {

        CustomAlertDialog(shouldShowDialog = shouldShowDialog, alarmName) { name ->

            viewModel.setAlarmName(name)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = DIMEN_15_dp),
                title = { Text("") },
                actions = {
                    Button(
                        onClick = {
                            viewModel.saveOrUpdateAlarm(context, alarmId)
                            navController.navigateUp()
                        },
                        enabled = isTimeValid
                    ) {
                        Text(if (alarmId == 0) "Save" else "Update")
                    }
                },
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.sz_close_icon),
                        contentDescription = null,
                        modifier = Modifier.clickable { navController.navigateUp() }
                    )
                }
            )
        },
        content = {

            Box(modifier = Modifier.fillMaxSize().padding(it)) {

                Column(modifier = Modifier.verticalScroll(scrollState).padding(DIMEN_15_dp)) {

                    TimeTextFieldView(viewModel)

                    CommonVerticalSpacer(DIMEN_15_dp)

                    Card(shape = RoundedCornerShape(DIMEN_10_dp), onClick = { shouldShowDialog.value = true }) {

                        Row(modifier = Modifier.padding(horizontal = DIMEN_10_dp, vertical = DIMEN_15_dp), verticalAlignment = Alignment.CenterVertically) {

                            Text(text = stringResource(R.string.sz_alarm_name), modifier = Modifier.padding(end = DIMEN_10_dp), style = MaterialTheme.typography.headlineSmall)

                            Text(text = alarmName.ifEmpty {  "Work" }, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.End)
                        }
                    }

                    CommonVerticalSpacer(DIMEN_15_dp)

                    Card(shape = RoundedCornerShape(DIMEN_10_dp)) {

                        Column(modifier = Modifier.padding(horizontal = DIMEN_10_dp, vertical = DIMEN_15_dp)) {

                            Text(text = stringResource(R.string.sz_repeat), style = MaterialTheme.typography.headlineSmall)

                            CommonVerticalSpacer(DIMEN_13_dp)

                            SuggestionChipLayout(repeatDays) { days ->

                                viewModel.setRepeatDays(days)
                            }
                        }
                    }

                    CommonVerticalSpacer(DIMEN_15_dp)

                    Card(shape = RoundedCornerShape(DIMEN_10_dp), onClick = {

                        val selectedRingtoneJson = Uri.encode(RingtoneItemTypeConverter().gson.toJson(ringtone))

                        navController.navigate("ringtone_settings/$selectedRingtoneJson")
                    }) {

                        Row(modifier = Modifier.padding(horizontal = DIMEN_10_dp, vertical = DIMEN_15_dp), verticalAlignment = Alignment.CenterVertically) {

                            Text(text = stringResource(R.string.sz_alarm_ringtone), modifier = Modifier.padding(end = DIMEN_10_dp), style = MaterialTheme.typography.headlineSmall)

                            Text(text = ringtone?.title ?: "Default", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.End)
                        }
                    }

                    CommonVerticalSpacer(DIMEN_15_dp)

                    Card(shape = RoundedCornerShape(DIMEN_10_dp)) {

                        Column(modifier = Modifier.padding(horizontal = DIMEN_10_dp, vertical = DIMEN_15_dp)) {

                            Text(text = stringResource(R.string.sz_alarm_volume), modifier = Modifier.padding(end = DIMEN_10_dp), style = MaterialTheme.typography.headlineSmall)

                            VolumeSlider(viewModel)
                        }
                    }

                    CommonVerticalSpacer(DIMEN_15_dp)

                    Card(shape = RoundedCornerShape(DIMEN_10_dp), onClick = { viewModel.setVibrate(!vibrate) }) {

                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = DIMEN_10_dp, vertical = DIMEN_5_dp), verticalAlignment = Alignment.CenterVertically) {

                            Text(text = stringResource(R.string.sz_vibrate), modifier = Modifier.weight(1f).padding(end = DIMEN_10_dp), style = MaterialTheme.typography.headlineSmall)

                            Switch(checked = vibrate, onCheckedChange = { viewModel.setVibrate(it) })
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun TimeTextFieldView(viewModel: AlarmDetailViewModel)
{
    val hourError = remember { mutableStateOf(false) }

    val hourErrorMessage = remember { mutableStateOf("") }

    val minuteError = remember { mutableStateOf(false) }

    val minuteErrorMessage = remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(DIMEN_10_dp)) {

        Column(modifier = Modifier.padding(horizontal = DIMEN_15_dp, vertical = 20.dp)) {

            Row {

                Surface(modifier = Modifier.weight(0.5f), color = MaterialTheme.colorScheme.surfaceVariant) {

                    TimeTextField(true, hourError, hourErrorMessage, viewModel)
                }

                Text(text = ":", modifier = Modifier.padding(horizontal = DIMEN_10_dp).align(Alignment.CenterVertically), color = MaterialTheme.colorScheme.onSurfaceVariant)

                Surface(modifier = Modifier.weight(0.5f), color = MaterialTheme.colorScheme.surfaceVariant) {

                    TimeTextField(false, minuteError, minuteErrorMessage, viewModel)
                }
            }

            if (hourError.value || minuteError.value)
            {
                CommonVerticalSpacer(DIMEN_5_dp)

                Row {

                    ShowErrorMessage(Modifier.weight(0.5f).padding(end = DIMEN_10_dp), hourErrorMessage.value)

                    ShowErrorMessage(Modifier.weight(0.5f).padding(start = DIMEN_10_dp), minuteErrorMessage.value)
                }
            }
        }
    }
}


@Composable
fun ShowErrorMessage(modifier: Modifier = Modifier, msg: String)
{
    Text(
        text = msg,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}




@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun AlarmDetailsPreview()
{
    SnoozelooTheme {

//        AlarmDetailScreen()
    }
}