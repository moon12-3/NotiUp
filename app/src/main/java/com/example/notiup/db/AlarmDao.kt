package com.example.notiup.db

import com.example.notiup.entity.Alarm
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): LiveData<MutableList<Alarm>>

    @Insert
    fun insert(alarm: Alarm)

    @Query("DELETE from alarm")
    fun deleteAll()

    @Delete
    fun delete(alarm: Alarm)

    // 모든 날짜, time_asc
    @Query("SELECT * FROM alarm ORDER BY sdate ASC, stime ASC")
    fun getAllAlarmSortedBySdate(): LiveData<MutableList<Alarm>>

    // 모든 날짜, time_desc
    @Query("SELECT * FROM alarm ORDER BY sdate DESC, stime DESC")
    fun getAllAlarmSortedBySdateDesc(): LiveData<MutableList<Alarm>>

    // 오늘 날짜, time_asc
    @Query("SELECT * FROM alarm WHERE sdate = :todayString ORDER BY sdate ASC, stime ASC")
    fun getTodayAlarmSortedBySdate(todayString: String): LiveData<MutableList<Alarm>>

    // 오늘 날짜, time_desc
    @Query("SELECT * FROM alarm WHERE sdate = :todayString ORDER BY sdate DESC, stime DESC")
    fun getTodayAlarmSortedBySdateDesc(todayString: String): LiveData<MutableList<Alarm>>

}