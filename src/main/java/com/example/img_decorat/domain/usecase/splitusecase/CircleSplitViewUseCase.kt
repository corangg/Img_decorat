package com.example.img_decorat.domain.usecase.splitusecase

import com.example.img_decorat.domain.repository.SplitRepository
import com.example.img_decorat.presentation.ui.view.SplitCircleView

class CircleSplitViewUseCase(private val splitRepository: SplitRepository) {
    fun excute(): SplitCircleView {
        return splitRepository.circleSplitView()
    }
}