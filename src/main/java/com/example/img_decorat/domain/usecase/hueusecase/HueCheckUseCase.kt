package com.example.img_decorat.domain.usecase.hueusecase

import com.example.img_decorat.data.model.dataModels.Hue
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.domain.repository.HueRepository
import java.util.LinkedList

class HueCheckUseCase(private val hueRepository: HueRepository) {
    fun excute(viewList: LinkedList<ViewItemData>, id: Int, hue: Hue): Hue?{
        return hueRepository.checkHue(viewList, id, hue)
    }
}