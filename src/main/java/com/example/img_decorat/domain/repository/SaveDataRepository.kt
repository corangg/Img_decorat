package com.example.img_decorat.domain.repository

import com.example.img_decorat.data.model.dataModels.LoadData
import com.example.img_decorat.data.model.dataModels.SaveViewData

interface SaveDataRepository {
    fun setLoadTitle(list : List<SaveViewData>): List<LoadData>
}