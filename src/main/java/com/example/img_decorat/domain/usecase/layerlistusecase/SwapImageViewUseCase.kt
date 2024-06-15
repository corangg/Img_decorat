package com.example.img_decorat.domain.usecase.layerlistusecase

import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.domain.repository.LayerListRepository
import java.util.LinkedList

class SwapImageViewUseCase(private val layerListRepository: LayerListRepository) {
    fun excute(
        viewList: LinkedList<ViewItemData>,
        fromPos: Int,
        toPos: Int
    ): LinkedList<ViewItemData> {
        return layerListRepository.swapImageView(viewList, fromPos, toPos)
    }
}