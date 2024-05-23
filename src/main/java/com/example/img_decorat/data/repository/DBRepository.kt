package com.example.img_decorat.data.repository

import com.example.img_decorat.data.model.dataModels.EmojiList
import com.example.img_decorat.data.source.local.Dao.EmojiDao
import javax.inject.Inject

class DBRepository @Inject constructor(
    private val emojiDao: EmojiDao
){
    suspend fun insertEmojiData(list: List<EmojiList>) {
        for (i in list) {
            emojiDao.insertEmojiList(i)
        }
    }

    suspend fun getEmojiData():List<EmojiList>?{
        return emojiDao.getEmojiList()
    }

    suspend fun deleteEmojiData(){
        emojiDao.deleteAllEmojiList()
    }


}