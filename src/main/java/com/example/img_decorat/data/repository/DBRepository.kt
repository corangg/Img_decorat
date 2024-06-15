package com.example.img_decorat.data.repository

import com.example.img_decorat.data.model.dataModels.EmojiDBData
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.data.source.local.Dao.EmojiDao
import com.example.img_decorat.data.source.local.Dao.ViewDao
import javax.inject.Inject

class DBRepository @Inject constructor(
    private val emojiDao: EmojiDao,
    private val viewDao: ViewDao
) {
    suspend fun insertEmojiData(list: List<EmojiDBData>) {
        for (i in list) {
            emojiDao.insertEmojiList(i)
        }
    }

    suspend fun getEmojiData(): List<EmojiDBData>? {
        return emojiDao.getEmojiList()
    }

    suspend fun deleteEmojiData() {
        emojiDao.deleteAllEmojiList()
    }

    suspend fun insertViewData(saveViewData: SaveViewData) {
        viewDao.insertSaveViewData(saveViewData)
    }

    suspend fun getAllViewData(): List<SaveViewData> {
        return viewDao.getSaveViewDataList()
    }

    suspend fun getViewData(key: String): SaveViewData? {
        return viewDao.getSaveViewDataByName(key)
    }

    suspend fun deleteAllViewData() {
        viewDao.deleteAllsaveViewDataList()
    }

    suspend fun deleteViewData(key: String) {
        viewDao.deletesaveViewDataList(key)
    }
}