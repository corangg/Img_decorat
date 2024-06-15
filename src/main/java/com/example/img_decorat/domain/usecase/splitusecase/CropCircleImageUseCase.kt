package com.example.img_decorat.domain.usecase.splitusecase

import android.graphics.Bitmap
import com.example.img_decorat.domain.repository.SplitRepository
import com.example.img_decorat.presentation.ui.view.SplitCircleView

class CropCircleImageUseCase(private val splitRepository: SplitRepository) {
    fun excute(circleView: SplitCircleView, bitmap: Bitmap): Bitmap {
        return splitRepository.cropCircleImage(circleView, bitmap)
    }
}