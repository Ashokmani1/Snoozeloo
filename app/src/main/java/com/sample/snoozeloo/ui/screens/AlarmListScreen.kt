package com.sample.snoozeloo.ui.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sample.snoozeloo.R
import com.sample.snoozeloo.ui.components.AlarmItem
import com.sample.snoozeloo.ui.theme.DIMEN_10_dp
import com.sample.snoozeloo.ui.theme.DIMEN_15_dp
import com.sample.snoozeloo.ui.theme.SnoozelooTheme
import com.sample.snoozeloo.viewmodel.AlarmViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmListScreen(navController: NavController,  viewModel: AlarmViewModel = hiltViewModel()) {

    val alarms by viewModel.alarms.collectAsState()

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

            FloatingActionButton(shape = CircleShape, onClick = { navController.navigate("alarm_details/${0}") }) {

                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.sz_add_alarm))
            }

        },

        floatingActionButtonPosition = FabPosition.Center,
    ) {

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
            LazyColumn(modifier = Modifier.fillMaxSize().padding(it).padding(horizontal = DIMEN_15_dp), contentPadding = PaddingValues(bottom = 70.dp)) {

                items(alarms) { alarm ->

                    AlarmItem(alarm, viewModel) {

                        navController.navigate("alarm_details/${alarm.id}")
                    }
                }
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

        AlarmListScreen(navController = rememberNavController())
    }
}