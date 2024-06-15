package com.example.img_decorat.domain.usecase.layerlistusecase

import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.domain.repository.LayerListRepository

class LoadListUseCase(private val layerListRepository: LayerListRepository) {
    fun excute(data: SaveViewData, viewSize: Int): ListData {
        return layerListRepository.loadList(data, viewSize)
    }
}