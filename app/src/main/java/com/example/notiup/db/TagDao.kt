package com.example.notiup.db

import com.example.notiup.entity.Tag
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TagDao {

    @Query("SELECT * FROM tag")
    fun tagLiveSelect() : LiveData<MutableList<Tag>>

    @Insert
    fun insert(tag: Tag)

    @Query("SELECT * FROM tag")
    fun getAllTags(): List<Tag>

    @Query("DELETE from tag")
    fun deleteAll()

}
