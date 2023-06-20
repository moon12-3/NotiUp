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

    @Query("SELECT * FROM alarm ORDER BY sdate, stime ASC")
    fun getAllAlarmSortedBySdate(): LiveData<MutableList<Alarm>>

    @Query("SELECT * FROM alarm ORDER BY sdate, stime DESC")
    fun getAllAlarmSortedBySdateDesc(): LiveData<MutableList<Alarm>>

    @Query("SELECT * FROM alarm WHERE sdate = :todayString")
    fun getTodayAlarm(todayString: String): LiveData<MutableList<Alarm>>

    @Query("SELECT * FROM alarm")
    fun getAllAlarm(): LiveData<MutableList<Alarm>>

    class AlarmRepository(private val alarmDao: AlarmDao) {
        fun getAlarmListBasedOnInputNumber(inputNumber: Int, inputNumber2: Int): LiveData<MutableList<Alarm>> {
            val alarmListLiveData = if (inputNumber == 0) { // 오름차순 선택
                alarmDao.getAllAlarmSortedBySdate()
            } else {    // 내림차순 선택
                alarmDao.getAllAlarmSortedBySdateDesc()
            }

            return Transformations.switchMap(alarmListLiveData) { alarmList ->
                if (inputNumber2 == 0) {    // 오늘 알람만
                    alarmDao.getTodayAlarm(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-M-d")))
                } else {    // 모든 알람
                    MutableLiveData(alarmList)
                }
            }
        }
    }
}
