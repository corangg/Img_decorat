package com.example.img_decorat.domain.repository

import android.graphics.Bitmap
import com.example.img_decorat.data.model.dataModels.EmojiDBData
import com.example.img_decorat.data.model.dataModels.EmojiData
import com.example.img_decorat.data.model.dataModels.EmojiList
import com.example.img_decorat.data.model.dataModels.ListData
import javax.inject.Singleton

interface EmojiRepository {

    fun emojiDataStringToBitmap(list: List<EmojiDBData>): List<EmojiList>

    fun emojiDBListClassification(list: List<EmojiData>): List<EmojiDBData>

    fun emojiAddLayer(listData: ListData, viewSize: Int, bitmap: Bitmap): ListData
}