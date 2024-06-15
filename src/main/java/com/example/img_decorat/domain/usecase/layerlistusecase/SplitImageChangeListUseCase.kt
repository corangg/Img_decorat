package com.example.img_decorat.domain.usecase.layerlistusecase

import android.net.Uri
import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.domain.repository.LayerListRepository

class SplitImageChangeListUseCase(private val layerListRepository: LayerListRepository) {
    fun excute(listData: ListData, uri: Uri): ListData {
        return layerListRepository.splitImageChangeList(listData, uri)
    }
}