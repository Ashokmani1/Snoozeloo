package com.sample.snoozeloo.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.sample.snoozeloo.R
import com.sample.snoozeloo.MainActivity
import com.sample.snoozeloo.model.Alarm

class AlarmReceiver : BroadcastReceiver()
{
    private var mediaPlayer: MediaPlayer? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent)
    {
        when (intent.action)
        {
            "CANCEL_ALARM" -> stopAlarm()

            else -> startAlarm(context, intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startAlarm(context: Context, intent: Intent)
    {
        val alarm = intent.getParcelableExtra("alarm") as? Alarm

        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (vibrator.hasVibrator())
        {
            val pattern = longArrayOf(0, 500, 500)
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        }

        mediaPlayer = MediaPlayer()
        val alarmUri = alarm?.ringtone?.uri ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        try
        {
            mediaPlayer?.apply {
                setDataSource(context, alarmUri)
                setAudioStreamType(AudioManager.STREAM_ALARM)
                isLooping = true // Loop the sound
                prepare()
                start()
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, alarm?.volume?.toInt() ?: 100, 0)

        showNotification(context, alarm)
    }

    private fun stopAlarm()
    {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun showNotification(context: Context, alarm: Alarm?)
    {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("alarm_channel", "Alarm Notifications", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(context, MainActivity::class.java).apply {

            putExtra("destination", "alarm_trigger")
            putExtra("alarm", alarm)
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, "alarm_channel")
            .setContentTitle("Alarm")
            .setContentText(alarm?.name)
            .setSmallIcon(R.drawable.sz_alarm_notification_icon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(alarm?.id ?: 0, notification)
    }
}