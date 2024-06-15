package com.example.img_decorat.domain.usecase

import android.widget.FrameLayout
import com.example.img_decorat.domain.repository.BackgroundRepository

class SetBackgroundScaleUseCase(private val backgroundRepository: BackgroundRepository) {
    fun execute(item: Int, screenWidth: Int): FrameLayout.LayoutParams {
        return backgroundRepository.setBackgroundScale(item, screenWidth)
    }
}