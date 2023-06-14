package com.example.notiup.db

import com.example.notiup.entity.Alarm
import com.example.notiup.entity.Tag
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class, Tag::class], version = 11)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun tagDao(): TagDao

    companion object{
        /* @Volatile = 접근 가능한 변수의 값을 cache를 통해 사용하지 않고
        thread가 직접 main memory에 접근하게 하여 동기화. */
        @Volatile
        private var instance : AppDatabase? = null

        // 싱글톤으로 생성. 이미 존재할 경우 생성하지 않고 바로 반환
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "notiup_db"
            ).fallbackToDestructiveMigration() // 스키마 충돌 시 데이터베이스 재구성
                .build()
        }

    }
}
