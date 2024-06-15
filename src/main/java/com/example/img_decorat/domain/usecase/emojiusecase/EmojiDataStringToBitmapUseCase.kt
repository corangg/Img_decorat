package com.example.img_decorat.domain.usecase.emojiusecase

import com.example.img_decorat.data.model.dataModels.EmojiDBData
import com.example.img_decorat.data.model.dataModels.EmojiList
import com.example.img_decorat.domain.repository.EmojiRepository

class EmojiDataStringToBitmapUseCase(private val emojiRepository: EmojiRepository) {
    fun excute(list: List<EmojiDBData>): List<EmojiList> {
        return emojiRepository.emojiDataStringToBitmap(list)
    }
}