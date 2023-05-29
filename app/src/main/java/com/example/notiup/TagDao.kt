import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TagDao {

    @Query("SELECT * FROM tag")
    fun tagLiveSelect() : LiveData<MutableList<Tag>>

    @Insert
    suspend fun insert(tag: Tag)

    @Query("SELECT * FROM tag")
    suspend fun getAllTags(): List<Tag>

    @Query("DELETE from tag")
    fun deleteAll()

}
