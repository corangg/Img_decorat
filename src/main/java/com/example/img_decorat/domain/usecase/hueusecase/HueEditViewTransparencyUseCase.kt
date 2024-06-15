package com.example.img_decorat.domain.usecase.hueusecase

import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.domain.repository.HueRepository
import java.util.LinkedList

class HueEditViewTransparencyUseCase(private val hueRepository: HueRepository) {
    fun excute(listData: ListData, transparency: Int) : LinkedList<ViewItemData> {
        return hueRepository.editViewTransparency(listData, transparency)
    }
}