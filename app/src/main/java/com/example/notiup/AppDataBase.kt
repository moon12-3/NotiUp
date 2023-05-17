package com.example.notiup

import AlarmDao
import androidx.room.RoomDatabase

abstract  class AppDataBase: RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

}