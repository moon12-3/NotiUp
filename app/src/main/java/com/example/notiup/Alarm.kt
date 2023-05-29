import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.firestore.auth.User
import java.util.*

@Entity(
    tableName = "alarm",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["u_id"],
            childColumns = ["u_id_fk"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["t_id"],
            childColumns = ["t_id_fk"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val a_id: Int = 0,
    val aname: String,
    val atext: String?,
    val sday: Date,
    val stime: Date,
    val eday: Date,
    val etime: Date,
    val repeat: Int,
    val amemo: String?,
    val lockscreen: Boolean,
    val noticenter: Boolean,
    val banner: Boolean,
    val user_id: Int,
    val tag_id: Int
)

