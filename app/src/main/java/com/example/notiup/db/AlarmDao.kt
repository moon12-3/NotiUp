package com.example.notiup.db

import com.example.notiup.entity.Alarm
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): LiveData<MutableList<Alarm>>

    @Insert
    fun insert(alarm: Alarm)

    @Query("DELETE from alarm")
    fun deleteAll()
}
