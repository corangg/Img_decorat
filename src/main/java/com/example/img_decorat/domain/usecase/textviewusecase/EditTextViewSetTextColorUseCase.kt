package com.example.img_decorat.domain.usecase.textviewusecase

import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.domain.repository.TextViewRepository

class EditTextViewSetTextColorUseCase(private val textViewRepository: TextViewRepository) {
    fun excute(listData: ListData, id: Int, color: Int) {
        return textViewRepository.editTextViewSetTextColor(listData, id, color)
    }
}