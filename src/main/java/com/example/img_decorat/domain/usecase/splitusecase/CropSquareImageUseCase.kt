package com.example.img_decorat.domain.usecase.splitusecase

import android.graphics.Bitmap
import com.example.img_decorat.domain.repository.SplitRepository
import com.example.img_decorat.presentation.ui.view.SplitSquareView

class CropSquareImageUseCase(private val splitRepository: SplitRepository) {
    fun excute(splitAreaView: SplitSquareView, bitmap: Bitmap): Bitmap {
        return splitRepository.cropSquareImage(splitAreaView, bitmap)
    }
}