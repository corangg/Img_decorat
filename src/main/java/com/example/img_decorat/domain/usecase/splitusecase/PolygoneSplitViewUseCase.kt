package com.example.img_decorat.domain.usecase.splitusecase

import com.example.img_decorat.domain.repository.SplitRepository
import com.example.img_decorat.presentation.ui.view.SplitPolygonView

class PolygoneSplitViewUseCase(private val splitRepository: SplitRepository) {
    fun excute(): SplitPolygonView {
        return splitRepository.polygoneSplitView()
    }
}