package com.sample.snoozeloo.ui

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sample.snoozeloo.model.Alarm
import com.sample.snoozeloo.model.RingtoneItem
import com.sample.snoozeloo.model.typeConverter.RingtoneItemTypeConverter
import com.sample.snoozeloo.ui.screens.AlarmDetailScreen
import com.sample.snoozeloo.ui.screens.AlarmListScreen
import com.sample.snoozeloo.ui.screens.AlarmTriggerScreen
import com.sample.snoozeloo.ui.screens.RingtoneSettingScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController, bundle: Bundle?)
{
    val destination = bundle?.getString("destination")

    NavHost(navController = navController, startDestination = destination ?: "alarm_list")
    {
        composable("alarm_list") {

            AlarmListScreen(navController)
        }

        addAlarmDetailScreen(navController)

        addRingtoneSettingScreen(navController)

        composable("alarm_trigger") {

            val alarm = bundle?.getParcelable("alarm") as? Alarm

            alarm?.let {

                AlarmTriggerScreen(alarm)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.addAlarmDetailScreen(navController: NavController)
{
    composable(
        route = "alarm_details/{alarmId}",
        arguments = listOf(navArgument("alarmId") {

            type = NavType.IntType

            defaultValue = 0
        })
    ) { backStackEntry ->

        val alarmId = backStackEntry.arguments?.getInt("alarmId") ?: 0

        AlarmDetailScreen(alarmId = alarmId, navController = navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.addRingtoneSettingScreen(navController: NavController) {
    composable(
        route = "ringtone_settings/{selectedRingtone}",
        arguments = listOf(navArgument("selectedRingtone") {
            type = NavType.StringType
            nullable = true
        })
    ) { backStackEntry ->

        val selectedRingtoneJson = backStackEntry.arguments?.getString("selectedRingtone")

        val selectedRingtone = selectedRingtoneJson?.let { json ->

            // Deserialize JSON to RingtoneItem
            RingtoneItemTypeConverter().gson.fromJson(json, RingtoneItem::class.java)
        }

        RingtoneSettingScreen(navController, selectedRingtone)
    }
}