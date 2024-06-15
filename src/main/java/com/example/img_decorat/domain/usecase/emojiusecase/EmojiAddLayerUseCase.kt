package com.example.img_decorat.domain.usecase.emojiusecase

import android.graphics.Bitmap
import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.domain.repository.EmojiRepository

class EmojiAddLayerUseCase(private val emojiRepository: EmojiRepository) {
    fun excute(listData: ListData, viewSize: Int, bitmap: Bitmap): ListData {
        return emojiRepository.emojiAddLayer(listData, viewSize, bitmap)
    }
}