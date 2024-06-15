package com.example.img_decorat.domain.usecase.textviewusecase

import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.domain.repository.TextViewRepository
import java.util.LinkedList

class EditTextViewSetTextSizeUseCase(private val textViewRepository: TextViewRepository) {
    fun excute(viewList: LinkedList<ViewItemData>, id: Int, size: Int) {
        return textViewRepository.editTextViewSetTextSize(viewList, id, size)
    }
}