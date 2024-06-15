package com.example.img_decorat.domain.usecase.textviewusecase

import android.graphics.Typeface
import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.domain.repository.TextViewRepository

class EditTextViewSetFontUseCase(private val textViewRepository: TextViewRepository) {
    fun excute(listData: ListData, id: Int, font: Typeface) {
        return textViewRepository.editTextViewSetFont(listData, id, font)
    }
}