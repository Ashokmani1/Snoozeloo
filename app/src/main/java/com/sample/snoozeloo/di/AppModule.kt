package com.sample.snoozeloo.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.sample.snoozeloo.model.AlarmDatabase
import com.sample.snoozeloo.repository.AlarmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule
{

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {

        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AlarmDatabase {
        return Room.databaseBuilder(
            context,
            AlarmDatabase::class.java,
            "alarm_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAlarmRepository(db: AlarmDatabase): AlarmRepository {

        return AlarmRepository(db.alarmDao())
    }
}