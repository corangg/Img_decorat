package com.example.img_decorat.domain.usecase.layerlistusecase

import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.domain.repository.LayerListRepository

class CheckedListUseCase(private val layerListRepository: LayerListRepository) {
    fun excute(listData: ListData, position: Int, checked: Boolean): ListData {
        return layerListRepository.checkedList(listData, position, checked)
    }
}