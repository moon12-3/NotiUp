import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.firestore.auth.User
import java.util.*

@Entity(
    tableName = "alarm",
    foreignKeys = [
//        ForeignKey(
//            entity = User::class,
//            parentColumns = ["u_id"],
//            childColumns = ["u_id_fk"],
//            onDelete = ForeignKey.CASCADE
//        ),
        ForeignKey(
            entity = Tag::class,            // 현재 entity 클래스가 참조하는 외부 entity
            parentColumns = ["t_id"],     // tag 클래스의 tag_id
            childColumns = ["t_id_fk"],     // 현재
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val a_id: Int = 0,
    val aname: String,
    val atext: String?,
    val sday: Long,
    val stime: Long,
    val eday: Long,
    val etime: Long,
    val repeat: Int,
    val amemo: String?,
    val lockscreen: Boolean,
    val noticenter: Boolean,
    val banner: Boolean,
//    val user_id: String?,
    val t_id_fk: Int
)
