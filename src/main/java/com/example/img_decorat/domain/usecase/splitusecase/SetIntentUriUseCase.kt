package com.example.img_decorat.domain.usecase.splitusecase

import android.graphics.Bitmap
import android.net.Uri
import com.example.img_decorat.domain.repository.SplitRepository

class SetIntentUriUseCase(private val splitRepository: SplitRepository) {
    fun excute(bitmap: Bitmap): Uri?{
        return splitRepository.setIntentUri(bitmap)
    }
}