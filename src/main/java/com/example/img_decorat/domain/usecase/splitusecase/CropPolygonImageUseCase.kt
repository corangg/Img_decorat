package com.example.img_decorat.domain.usecase.splitusecase

import android.graphics.Bitmap
import com.example.img_decorat.domain.repository.SplitRepository
import com.example.img_decorat.presentation.ui.view.SplitPolygonView

class CropPolygonImageUseCase(private val splitRepository: SplitRepository) {
    fun excute(splitAreaView: SplitPolygonView, bitmap: Bitmap): Bitmap {
        return splitRepository.cropPolygonImage(splitAreaView, bitmap)
    }
}