package com.example.img_decorat.domain.usecase.layerlistusecase

import android.net.Uri
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.domain.repository.LayerListRepository
import java.util.LinkedList

class SetLastTouchedImageUseCase(private val layerListRepository: LayerListRepository) {
    fun excute(layerList: LinkedList<LayerItemData>, id: Int): Uri? {
        return layerListRepository.setLastTouchedImage(layerList, id)
    }
}