package com.example.img_decorat.domain.repository

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import java.util.LinkedList

interface LayerListRepository {
    fun imgAddList(data: Intent?, listData: ListData, viewSize: Int): ListData?

    fun checkedList(listData: ListData, position: Int, checked: Boolean): ListData

    fun deleteList(listData: ListData, position: Int): ListData

    fun selectLayer(layerList: LinkedList<LayerItemData>, position: Int): LinkedList<LayerItemData>

    fun checkLastSelectImage(layerList: LinkedList<LayerItemData>, id: Int): Boolean

    fun selectLastImage(layerList: LinkedList<LayerItemData>, id: Int): LinkedList<LayerItemData>

    fun swapImageView(
        viewList: LinkedList<ViewItemData>,
        fromPos: Int,
        toPos: Int
    ): LinkedList<ViewItemData>

    fun setLastTouchedImage(layerList: LinkedList<LayerItemData>, id: Int): Uri?

    fun splitImageChangeList(listData: ListData, uri: Uri): ListData

    fun checkedViewType(layerList: LinkedList<LayerItemData>, id: Int?): Int

    fun loadList(data: SaveViewData, viewSize: Int): ListData

    fun imageAddViewList(
        viewList: LinkedList<ViewItemData>,
        id: Int,
        bitmap: Bitmap?,
        visibility: Boolean,
        scale: Float = 1.0f
    ): LinkedList<ViewItemData>?
}