package com.example.img_decorat.domain.usecase

import android.widget.FrameLayout
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.domain.repository.ImageManagementRepository

class SaveViewUseCase(private val imageManagementRepository: ImageManagementRepository) {

    fun execute(
        list: List<ViewItemData>,
        view: FrameLayout,
        scale: Float,
        name: String
    ): SaveViewData {
        return imageManagementRepository.saveView(list, view, scale, name)
    }
}