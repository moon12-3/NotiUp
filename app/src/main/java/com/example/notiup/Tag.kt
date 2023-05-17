import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.firestore.auth.User

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["u_id"],
            childColumns = ["u_id_fk"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val t_id: Int = 0,
    val tcolor: Int,
    val ttext: String,
    val u_id: Int
)
