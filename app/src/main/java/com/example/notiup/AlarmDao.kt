import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): List<Alarm>

    @Insert
    fun insert(alarm: Alarm)

    @Query("DELETE from alarm")
    fun deleteAll()
}