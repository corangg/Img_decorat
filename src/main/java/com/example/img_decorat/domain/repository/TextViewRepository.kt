package com.example.img_decorat.domain.repository

import android.graphics.Typeface
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import java.util.LinkedList

interface TextViewRepository {
    fun checkEditableTextView(viewList: LinkedList<ViewItemData>, id: Int): Boolean

    fun editTextViewAddList(listData: ListData, viewSize: Int): ListData

    fun editTextViewSetTextColor(listData: ListData, id: Int, color: Int)

    fun editTextViewSetBackgroundColor(listData: ListData, id: Int, color: Int)

    fun editTextViewSetTextSize(viewList: LinkedList<ViewItemData>, id: Int, size: Int)

    fun editTextViewSetFont(listData: ListData, id: Int, font: Typeface)

    fun layerViewUpdateText(listData: ListData, id: Int, textData: String)

    fun addEditTextViewViewList(
        layerList: LinkedList<LayerItemData>,
        textId: Int,
        textValue: String
    ): LinkedList<LayerItemData>

    fun editTextViewAddViewList(
        viewList: LinkedList<ViewItemData>,
        viewSize: Int,
        addId: Int,
        visibility: Boolean
    ): LinkedList<ViewItemData>

    fun layerTextViewSetTextColor(layerList: LinkedList<LayerItemData>, id: Int, color: Int)

    fun layerTextViewSetTextBackgroundColor(
        layerList: LinkedList<LayerItemData>,
        id: Int,
        color: Int
    )

    fun layerTextViewSetTextFont(layerList: LinkedList<LayerItemData>, id: Int, font: Typeface)
}