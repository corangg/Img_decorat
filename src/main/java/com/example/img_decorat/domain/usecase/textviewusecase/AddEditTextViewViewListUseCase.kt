package com.example.img_decorat.domain.usecase.textviewusecase

import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.domain.repository.TextViewRepository
import java.util.LinkedList

class AddEditTextViewViewListUseCase(private val textViewRepository: TextViewRepository) {
    fun excute(
        layerList: LinkedList<LayerItemData>,
        textId: Int,
        textValue: String
    ): LinkedList<LayerItemData> {
        return textViewRepository.addEditTextViewViewList(layerList, textId, textValue)
    }
}