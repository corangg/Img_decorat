package com.example.img_decorat.data.source.local.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.img_decorat.data.model.dataModels.EmojiDBData
import com.example.img_decorat.data.model.dataModels.EmojiList
import kotlinx.coroutines.flow.Flow

@Dao
interface EmojiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmojiList(emojiDBList: EmojiDBData)

    @Query("SELECT * FROM emoji_list")
    suspend fun getEmojiList(): List<EmojiDBData>?

    @Query("DELETE FROM emoji_list")
    suspend fun deleteAllEmojiList()
}