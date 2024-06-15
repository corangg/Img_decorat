package com.example.img_decorat.domain.usecase.saveusecase

import com.example.img_decorat.data.model.dataModels.LoadData
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.domain.repository.SaveDataRepository

class SaveSetLoadTitleUseCase(private val saveDataRepository: SaveDataRepository) {
    fun excute(list : List<SaveViewData>): List<LoadData>{
        return saveDataRepository.setLoadTitle(list)
    }
}