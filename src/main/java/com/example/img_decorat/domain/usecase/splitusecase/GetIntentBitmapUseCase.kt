package com.example.img_decorat.domain.usecase.splitusecase

import android.graphics.Bitmap
import android.net.Uri
import com.example.img_decorat.domain.repository.SplitRepository

class GetIntentBitmapUseCase(private val splitRepository: SplitRepository) {
    fun excute(uri: Uri): Bitmap?{
        return splitRepository.getIntentBitmap(uri)
    }
}