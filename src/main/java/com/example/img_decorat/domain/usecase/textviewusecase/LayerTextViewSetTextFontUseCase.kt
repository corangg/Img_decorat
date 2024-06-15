package com.example.img_decorat.domain.usecase.textviewusecase

import android.graphics.Typeface
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.domain.repository.TextViewRepository
import java.util.LinkedList

class LayerTextViewSetTextFontUseCase(private val textViewRepository: TextViewRepository) {
    fun excute(layerList: LinkedList<LayerItemData>, id: Int, font: Typeface) {
        return textViewRepository.layerTextViewSetTextFont(layerList, id, font)
    }
}