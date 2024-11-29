package com.sample.snoozeloo.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.sample.snoozeloo.model.Alarm
import java.util.*

object AlarmManagerUtil
{

    @RequiresApi(Build.VERSION_CODES.O)
    fun setAlarm(context: Context, alarm: Alarm)
    {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (day in alarm.repeatDays) {

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("alarm", alarm)
            }

            val requestCode = alarm.id * 10 + day.value

            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val calendar = Calendar.getInstance().apply {

                timeInMillis = System.currentTimeMillis()

                set(Calendar.HOUR_OF_DAY, alarm.hour)
                set(Calendar.MINUTE, alarm.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                // Adjust calendar to the next occurrence of the repeat day
                while (get(Calendar.DAY_OF_WEEK) != day.value) {

                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }


            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelAlarm(context: Context, alarm: Alarm)
    {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, alarm.id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)

        val cancelIntent = Intent(context, AlarmReceiver::class.java).apply {

            action = "CANCEL_ALARM"
        }

        context.sendBroadcast(cancelIntent)
    }
}