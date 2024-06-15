package com.example.img_decorat.domain.usecase.textviewusecase

import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.domain.repository.TextViewRepository
import java.util.LinkedList

class LayerTextViewSetTextBackgroundColorUseCase(private val textViewRepository: TextViewRepository) {
    fun excute(
        layerList: LinkedList<LayerItemData>,
        id: Int,
        color: Int
    ) {
        return textViewRepository.layerTextViewSetTextBackgroundColor(layerList, id, color)
    }
}