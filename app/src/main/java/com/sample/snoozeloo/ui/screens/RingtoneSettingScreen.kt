package com.sample.snoozeloo.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sample.snoozeloo.R
import com.sample.snoozeloo.model.RingtoneItem
import com.sample.snoozeloo.ui.components.CommonHorizontalSpacer
import com.sample.snoozeloo.ui.theme.DIMEN_10_dp
import com.sample.snoozeloo.ui.theme.DIMEN_15_dp
import com.sample.snoozeloo.ui.theme.DIMEN_5_dp
import com.sample.snoozeloo.ui.theme.DIME_7_dp
import com.sample.snoozeloo.ui.theme.SnoozelooTheme
import com.sample.snoozeloo.viewmodel.RingtoneViewModel



@Composable
fun RingtoneList(viewModel: RingtoneViewModel) {

    val ringtones by viewModel.ringtones.collectAsState()

    val context = LocalContext.current

    val selectedRingtone by viewModel.selectedRingtone.collectAsState()

    LazyColumn {

        itemsIndexed(ringtones) { index, ringtone ->

            Card(shape = RoundedCornerShape(DIMEN_10_dp), modifier = Modifier.padding(DIMEN_5_dp), onClick = {
                    viewModel.selectRingtone(context, ringtone)
                }
            ) {

                Row(modifier = Modifier.fillMaxWidth().padding(DIMEN_10_dp), verticalAlignment = Alignment.CenterVertically) {

                    CircularImageView(index)

                    CommonHorizontalSpacer(DIME_7_dp)

                    Text(text = ringtone.title, modifier = Modifier.weight(1f).padding(end = DIMEN_10_dp), style = MaterialTheme.typography.headlineSmall)

                    if (ringtone.title == selectedRingtone?.title)
                    {
                        Box(
                            modifier = Modifier
                                .size(20.dp) // Set the size of the circle
                                .clip(CircleShape) // Clip to a circular shape
                                .background(MaterialTheme.colorScheme.primary).padding(1.dp) // Set background color
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.sz_tick_icon), // Replace with your image resource
                                contentDescription = "Circular Image",
                                modifier = Modifier
                                    .matchParentSize() // Match the size of the parent box
                                    .clip(CircleShape) // Clip image to circle
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CircularImageView(index: Int) {

    Box(
        modifier = Modifier
            .size(25.dp) // Set the size of the circle
            .clip(CircleShape) // Clip to a circular shape
            .background(MaterialTheme.colorScheme.onSurfaceVariant).padding(6.dp) // Set background color
    ) {
        Image(
            painter = painterResource(id = if (index == 0) R.drawable.sz_silent_icon else R.drawable.sz_ringtone_icon), // Replace with your image resource
            contentDescription = "Circular Image",
            modifier = Modifier
                .matchParentSize() // Match the size of the parent box
                .clip(CircleShape) // Clip image to circle
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RingtoneSettingScreen(navController: NavController, selectedRingtone: RingtoneItem?, viewModel: RingtoneViewModel = hiltViewModel())
{
    val selectedRingtoneState by viewModel.selectedRingtone.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {

        viewModel.fetchRingtones(context)

        selectedRingtone?.let {

            viewModel.setSelectedRingtone(it)
        }
    }

    Scaffold(

        topBar = {

            TopAppBar(

                modifier = Modifier.heightIn(max = 56.dp).padding(horizontal = DIMEN_15_dp),

                actions = { Button(
                    onClick = {
                        selectedRingtoneState?.let { ringtoneItem ->

                            viewModel.stopCurrentRingtone()
                            navController.previousBackStackEntry?.savedStateHandle?.set("selectedRingtone", ringtoneItem)
                            navController.navigateUp()
                        }
                    }
                ) { Text("Save") } },

                title = { Text("Ringtones", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.fillMaxWidth().padding(start = DIMEN_10_dp)) },

                navigationIcon = { Image(painter = painterResource(R.drawable.sz_close_icon), contentDescription = null, modifier = Modifier.clickable { navController.navigateUp() }) }
            )
        },

        content = {

            Column(modifier = Modifier.fillMaxSize().padding(it).padding(horizontal = DIMEN_15_dp)) {

                RingtoneList(viewModel)
            }
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun RingtoneSettingScreenPreview()
{
    SnoozelooTheme {

        RingtoneSettingScreen(rememberNavController(), null)
    }
}
