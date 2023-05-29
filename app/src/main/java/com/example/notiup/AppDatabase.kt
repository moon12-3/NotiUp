package com.example.notiup

import AlarmDao
import TagDao
import androidx.room.RoomDatabase

abstract  class AppDataBase: RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun tagDao(): TagDao
}