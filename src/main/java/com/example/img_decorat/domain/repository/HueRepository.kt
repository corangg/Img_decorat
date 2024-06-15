package com.example.img_decorat.domain.repository

import com.example.img_decorat.data.model.dataModels.Hue
import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import java.util.LinkedList

interface HueRepository {
    fun checkHue(viewList: LinkedList<ViewItemData>, id: Int, hue: Hue): Hue?

    fun editViewSaturation(listData: ListData, saturation: Int) : LinkedList<ViewItemData>

    fun editViewBrightness(listData: ListData, brightness: Int) : LinkedList<ViewItemData>

    fun editViewTransparency(listData: ListData, transparency: Int) : LinkedList<ViewItemData>
}