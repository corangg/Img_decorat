package com.example.img_decorat.domain.usecase.layerlistusecase

import android.content.Intent
import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.domain.repository.LayerListRepository

class ImageAddListUseCase(private val layerListRepository: LayerListRepository) {
    fun execute(data: Intent?, listData: ListData, viewSize: Int): ListData? {
        return layerListRepository.imgAddList(data, listData, viewSize)
    }
}