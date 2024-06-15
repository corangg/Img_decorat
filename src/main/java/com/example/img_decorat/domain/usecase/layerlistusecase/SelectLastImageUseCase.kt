package com.example.img_decorat.domain.usecase.layerlistusecase

import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.domain.repository.LayerListRepository
import java.util.LinkedList

class SelectLastImageUseCase(private val layerListRepository: LayerListRepository) {
    fun excute(layerList: LinkedList<LayerItemData>, id: Int): LinkedList<LayerItemData> {
        return layerListRepository.selectLastImage(layerList, id)
    }
}