package com.example.img_decorat.data.source.local.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.img_decorat.data.model.dataModels.EmojiList
import kotlinx.coroutines.flow.Flow

@Dao
interface EmojiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmojiList(emojiList: EmojiList)

    @Query("SELECT * FROM emoji_data")
    suspend fun getEmojiList(): List<EmojiList>?

    @Query("DELETE FROM emoji_data")
    suspend fun deleteAllEmojiList()
}