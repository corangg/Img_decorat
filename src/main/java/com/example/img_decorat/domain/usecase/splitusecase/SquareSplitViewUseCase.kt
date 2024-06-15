package com.example.img_decorat.domain.usecase.splitusecase

import com.example.img_decorat.domain.repository.SplitRepository
import com.example.img_decorat.presentation.ui.view.SplitSquareView

class SquareSplitViewUseCase(private val splitRepository: SplitRepository) {
    fun excute(): SplitSquareView {
        return splitRepository.squareSplitView()
    }
}