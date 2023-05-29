import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notiup.Converters

@Database(entities = [Alarm::class, Tag::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

    companion object{
        /* @Volatile = 접근 가능한 변수의 값을 cache를 통해 사용하지 않고
        thread가 직접 main memory에 접근하게 하여 동기화. */
        @Volatile
        private var instance : AppDatabase? = null

        // 싱글톤으로 생성. 이미 존재할 경우 생성하지 않고 바로 반환
        fun getDatabase(context : Context) : AppDatabase? {
            if(instance == null){
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "notiup_db"
                    ).build()
                }
            }
            return instance
        }


    }
}
