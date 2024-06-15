package com.example.img_decorat.domain.usecase

import com.example.img_decorat.data.model.dataModels.SaveViewDataInfo
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.domain.repository.ImageManagementRepository

class SaveViewDataSetUseCase(private val imageManagementRepository: ImageManagementRepository) {

    fun execute(list: List<ViewItemData>): List<SaveViewDataInfo> {
        return imageManagementRepository.saveViewDataSet(list)
    }
}