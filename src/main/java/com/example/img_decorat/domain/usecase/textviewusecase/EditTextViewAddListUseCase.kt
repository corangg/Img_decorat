package com.example.img_decorat.domain.usecase.textviewusecase

import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.domain.repository.TextViewRepository

class EditTextViewAddListUseCase(private val textViewRepository: TextViewRepository) {
    fun excute(listData: ListData, viewSize: Int): ListData {
        return textViewRepository.editTextViewAddList(listData, viewSize)
    }
}