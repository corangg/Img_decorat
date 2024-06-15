package com.example.img_decorat.data.repository

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.widget.FrameLayout
import android.widget.TextView
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.domain.repository.TextViewRepository
import com.example.img_decorat.utils.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextViewRepositoryImpl  @Inject constructor(
    @ApplicationContext private val context: Context
) : TextViewRepository {
    override fun checkEditableTextView(viewList: LinkedList<ViewItemData>, id: Int): Boolean {
        val findItem = viewList.find { it.id == id }
        findItem?.let {
            if (it.type == 1) {
                return false
            }
        }
        return true
    }

    override fun editTextViewAddList(listData: ListData, viewSize: Int): ListData {
        val textId = Util.setID()
        return ListData(
            addEditTextViewViewList(listData.layerList, textId, ""),
            editTextViewAddViewList(listData.viewList, viewSize, textId, true))
    }

    override fun editTextViewSetTextColor(listData: ListData, id: Int, color: Int) {
        Util.getLastSelectView(listData.viewList, id)?.let {
            if (it.type == 1) {
                layerTextViewSetTextColor(listData.layerList, id, color)
                it.text.apply {
                    setTextColor(color)
                }
            }
        }
    }

    override fun editTextViewSetBackgroundColor(listData: ListData, id: Int, color: Int) {
        Util.getLastSelectView(listData.viewList, id)?.let {
            if (it.type == 1) {
                layerTextViewSetTextBackgroundColor(listData.layerList, id, color)
                it.text.apply {
                    setBackgroundClolor(color)
                }
            }
        }
    }

    override fun editTextViewSetTextSize(viewList: LinkedList<ViewItemData>, id: Int, size: Int){
        Util.getLastSelectView(viewList, id)?.let {
            if (it.type == 1) {
                it.text.apply {
                    setTextSize(size.toFloat())
                }
            }
        }
    }

    override fun editTextViewSetFont(listData: ListData, id: Int, font: Typeface){
        Util.getLastSelectView(listData.viewList, id)?.let {
            if (it.type == 1) {
                layerTextViewSetTextFont(listData.layerList, id, font)
                it.text.apply {
                    typeface = font
                }
            }
        }
    }

    override fun layerViewUpdateText(listData: ListData, id: Int, textData: String) {
        Util.getLastSelectView(listData.viewList, id)?.let {
            if(it.type == 1){
                listData.layerList.find { it.select }?.let {layerItem ->
                    layerItem.text.apply {
                        text = textData
                    }
                }
            }
        }
    }

    override fun addEditTextViewViewList(layerList: LinkedList<LayerItemData>, textId: Int, textValue: String) : LinkedList<LayerItemData>{
        val layerItemData = LayerItemData(context = context, check = true, id = textId, type = 1)
        layerItemData.text = TextView(context).apply {
            id = textId
            setBackgroundColor(Color.TRANSPARENT)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            text = textValue
        }
        layerList.add(layerItemData)
        return layerList
    }

    override fun editTextViewAddViewList(viewList: LinkedList<ViewItemData>, viewSize: Int, addId: Int, visibility: Boolean): LinkedList<ViewItemData> {
        val editView = Util.createEditableTextView(
            context = context,
            viewId = addId,
            withSize = viewSize,
            heightSize = viewSize
        )
        val viewData =
            ViewItemData(context = context, id = editView.id, visible = visibility, type = 1)
        viewData.text = editView
        viewList.add(viewData)
        return viewList
    }

    override fun layerTextViewSetTextColor(layerList: LinkedList<LayerItemData>, id: Int, color: Int) {
        layerList.find { it.id == id }?.let {
            it.text.apply {
                setTextColor(color)
            }
        }
    }

    override fun layerTextViewSetTextBackgroundColor(layerList: LinkedList<LayerItemData>, id: Int, color: Int)  {
        layerList.find { it.id == id }?.let {
            it.text.apply {
                setBackgroundColor(color)
            }
        }
    }

    override fun layerTextViewSetTextFont(layerList: LinkedList<LayerItemData>, id: Int, font: Typeface){
        layerList.find { it.id == id }?.let {
            it.text.apply {
                typeface = font
            }
        }
    }
}