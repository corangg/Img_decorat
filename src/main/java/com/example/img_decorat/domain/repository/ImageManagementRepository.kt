package com.example.img_decorat.domain.repository

import android.view.View
import android.widget.FrameLayout
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.data.model.dataModels.SaveViewDataInfo
import com.example.img_decorat.data.model.dataModels.ViewItemData

interface ImageManagementRepository {

    fun editViewSave(view: View, fileName: String)

    fun saveView(
        list: List<ViewItemData>,
        view: FrameLayout,
        scale: Float,
        name: String
    ): SaveViewData
}