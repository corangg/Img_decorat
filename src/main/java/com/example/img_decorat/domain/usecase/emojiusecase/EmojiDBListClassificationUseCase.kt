package com.example.img_decorat.domain.usecase.emojiusecase

import com.example.img_decorat.data.model.dataModels.EmojiDBData
import com.example.img_decorat.data.model.dataModels.EmojiData
import com.example.img_decorat.domain.repository.EmojiRepository

class EmojiDBListClassificationUseCase(private val emojiRepository: EmojiRepository) {
    fun excute(list: List<EmojiData>): List<EmojiDBData> {
        return emojiRepository.emojiDBListClassification(list)
    }
}