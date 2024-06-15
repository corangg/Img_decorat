package com.example.img_decorat.domain.usecase.layerlistusecase

import android.graphics.Bitmap
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.domain.repository.LayerListRepository
import java.util.LinkedList

class ImageAddViewUseCase(private val layerListRepository: LayerListRepository) {
    fun execute(
        viewList: LinkedList<ViewItemData>,
        id: Int,
        bitmap: Bitmap?,
        visibility: Boolean,
        scale: Float = 1.0f
    ): LinkedList<ViewItemData>? {
        return layerListRepository.imageAddViewList(viewList, id, bitmap, visibility, scale)
    }
}