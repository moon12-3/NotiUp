package com.example.notiup.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "alarm",    // 알람 테이블
//    foreignKeys = [
////        ForeignKey(
////            entity = User::class,
////            parentColumns = ["u_id"],
////            childColumns = ["u_id_fk"],
////            onDelete = ForeignKey.CASCADE
////        ),
//        ForeignKey(
//            entity = Tag::class,            // 현재 entity 클래스가 참조하는 외부 entity
//            parentColumns = ["t_id"],     // tag 클래스의 tag_id
//            childColumns = ["t_id_fk"],     // 현재
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
)
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val a_id: Int = 0,
    val atitle: String,
    val sdate: String,
    val stime: String,
    val edate: String,
    val etime: String,
    val repeat: Int,
    val amemo: String,
//    val t_id_fk: Int
)
