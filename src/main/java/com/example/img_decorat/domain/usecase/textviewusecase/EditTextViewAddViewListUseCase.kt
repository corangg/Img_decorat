package com.example.img_decorat.domain.usecase.textviewusecase

import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.domain.repository.TextViewRepository
import java.util.LinkedList

class EditTextViewAddViewListUseCase(private val textViewRepository: TextViewRepository) {
    fun excute(
        viewList: LinkedList<ViewItemData>,
        viewSize: Int,
        addId: Int,
        visibility: Boolean
    ): LinkedList<ViewItemData> {
        return textViewRepository.editTextViewAddViewList(viewList, viewSize, addId, visibility)
    }
}