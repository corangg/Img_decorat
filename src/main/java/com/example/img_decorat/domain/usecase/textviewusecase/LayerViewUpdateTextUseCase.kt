package com.example.img_decorat.domain.usecase.textviewusecase

import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.domain.repository.TextViewRepository

class LayerViewUpdateTextUseCase(private val textViewRepository: TextViewRepository) {
    fun excute(listData: ListData, id: Int, textData: String) {
        return textViewRepository.layerViewUpdateText(listData, id, textData)
    }
}