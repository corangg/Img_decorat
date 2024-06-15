package com.example.img_decorat.data.repository

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.data.model.dataModels.SaveViewDataInfo
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.domain.repository.LayerListRepository
import com.example.img_decorat.utils.Util.bitmapToUri
import com.example.img_decorat.utils.Util.createEditableImageView
import com.example.img_decorat.utils.Util.resizeBitmap
import com.example.img_decorat.utils.Util.setID
import com.example.img_decorat.utils.Util.uriToBitmap
import com.example.img_decorat.utils.UtilList
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Collections
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LayerListRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val textViewRepositoryImpl: TextViewRepositoryImpl
) : LayerListRepository {
    override fun imgAddList(data: Intent?, listData: ListData, viewSize: Int): ListData? {
        data?.clipData?.let {
            for (i in 0 until it.itemCount) {
                val imageUri: Uri = it.getItemAt(i).uri
                val bitmap = uriToBitmap(context = context, imageUri = imageUri)
                val id = setID()
                bitmap?.let {
                    val resizeBitmap = resizeBitmap(bitmap, viewSize)
                    val layerList = addLayerList(listData.layerList, id, resizeBitmap.first, false)
                    val viewList = imageAddViewList(
                        listData.viewList,
                        id,
                        resizeBitmap.first,
                        false,
                        scale = 1 / resizeBitmap.second
                    )
                    layerList?.let { nonNullLayerList ->
                        viewList?.let { nonNullViewList ->
                            return ListData(nonNullLayerList, nonNullViewList)
                        }
                    }
                }
            }
        }
        return null
    }

    override fun checkedList(listData: ListData, position: Int, checked: Boolean): ListData {
        return ListData(
            checkedUpdateLayerList(listData.layerList, position, checked),
            checkedUpdateViewList(listData, position, checked)
        )
    }

    override fun deleteList(listData: ListData, position: Int): ListData {
        return ListData(
            deleteLayerList(listData.layerList, position),
            deleteImageViewList(listData.viewList, position)
        )
    }

    override fun selectLayer(
        layerList: LinkedList<LayerItemData>,
        position: Int
    ): LinkedList<LayerItemData> {
        val selectedItem = layerList.find { it.select }

        selectedItem?.let {
            it.select = false
        }

        layerList[position].select = true
        return layerList
    }

    override fun checkLastSelectImage(layerList: LinkedList<LayerItemData>, id: Int): Boolean {
        layerList.find { it.select }?.let {
            if (it.id == id) {
                return false
            } else {
                return true
            }
        } ?: run {
            selectLastImage(layerList, id)
            return true
        }
    }

    override fun selectLastImage(
        layerList: LinkedList<LayerItemData>,
        id: Int
    ): LinkedList<LayerItemData> {
        val selectedItem = layerList.find { it.select }
        val findItem = layerList.find { it.id == id }

        selectedItem?.let {
            it.select = false
        }

        findItem?.let {
            it.select = true
        }

        return layerList
    }

    override fun swapImageView(
        viewList: LinkedList<ViewItemData>,
        fromPos: Int,
        toPos: Int
    ): LinkedList<ViewItemData> {
        Collections.swap(viewList, fromPos, toPos)
        return viewList
    }

    override fun setLastTouchedImage(layerList: LinkedList<LayerItemData>, id: Int): Uri? {
        val image = layerList.find { it.id == id }
        image?.let {
            return bitmapToUri(context = context, it.bitMap)
        }
        return null
    }

    override fun splitImageChangeList(listData: ListData, uri: Uri): ListData {//아닌가?
        val bitmap = uriToBitmap(context = context, imageUri = uri)
        val selectedItem = listData.layerList.find { it.select }
        selectedItem?.let { item ->
            bitmap?.let {
                item.bitMap = it
                changeViewList(listData.viewList, item.id, it)
            }
        }
        return listData
    }

    override fun checkedViewType(layerList: LinkedList<LayerItemData>, id: Int?): Int {
        val selectItem = layerList.find { it.id == id }
        selectItem?.let {
            return it.type
        }
        return -1
    }


    override fun loadList(data: SaveViewData, viewSize: Int): ListData {
        val layerList = setLoadLayerData(data.data)
        val viewList = setLoadViewData(layerList, viewSize, data.data)
        return ListData(layerList, viewList)
    }

    override fun imageAddViewList(
        viewList: LinkedList<ViewItemData>,
        id: Int,
        bitmap: Bitmap?,
        visibility: Boolean,
        scale: Float
    ): LinkedList<ViewItemData>? {
        bitmap?.let {
            val imageView =
                createEditableImageView(context = context, viewId = id, bitmap = it, scale = scale)
            val viewData = ViewItemData(context = context, id = imageView.id, visible = visibility)
            viewData.img = imageView
            viewList.add(viewData)
            return viewList
        }
        return null
    }


    private fun addLayerList(
        layerList: LinkedList<LayerItemData>,
        id: Int,
        bitmap: Bitmap?,
        check: Boolean
    ): LinkedList<LayerItemData>? {
        bitmap?.let {
            val layerItemData =
                LayerItemData(context = context, check = check, id = id, bitMap = it)
            layerList.add(layerItemData)
            return layerList
        }
        return null
    }

    private fun checkedUpdateLayerList(
        layerList: LinkedList<LayerItemData>,
        position: Int,
        checked: Boolean
    ): LinkedList<LayerItemData> {
        val layerItemData: LayerItemData = layerList[position]
        layerItemData.check = checked

        layerList.set(position, layerItemData)
        return layerList
    }

    private fun checkedUpdateViewList(
        listData: ListData,
        position: Int,
        checked: Boolean
    ): LinkedList<ViewItemData> {
        val targetItem = listData.viewList.find { it.id == listData.layerList[position].id }

        targetItem?.let {
            if (checked) {
                it.visible = true
            } else {
                it.visible = false
            }
        }
        return listData.viewList
    }

    private fun deleteLayerList(
        layerList: LinkedList<LayerItemData>,
        position: Int
    ): LinkedList<LayerItemData> {
        layerList.removeAt(position)
        return layerList
    }

    private fun deleteImageViewList(
        viewList: LinkedList<ViewItemData>,
        position: Int
    ): LinkedList<ViewItemData> {
        viewList.removeAt(position)
        return viewList
    }


    private fun setLoadLayerData(list: List<SaveViewDataInfo>): LinkedList<LayerItemData> {
        var layerList = LinkedList<LayerItemData>()
        for (i in list) {
            val idValue = setID()
            when (i.type) {
                0 -> {
                    val bitmap = uriToBitmap(context, i.img.toUri())
                    bitmap?.let {
                        addLayerList(layerList, idValue, it, i.visible)?.let { list ->
                            layerList = list
                        }
                    }
                }

                1 -> {
                    layerList =
                        textViewRepositoryImpl.addEditTextViewViewList(layerList, idValue, i.text)
                    layerList = textViewRepositoryImpl.layerTextViewSetTextColor(
                        layerList,
                        idValue,
                        i.textColor
                    )
                    layerList = textViewRepositoryImpl.layerTextViewSetTextBackgroundColor(
                        layerList,
                        idValue,
                        i.bgColor
                    )
                    layerList = textViewRepositoryImpl.layerTextViewSetTextFont(
                        layerList,
                        idValue,
                        UtilList.typefaces[i.font]
                    )
                }
            }
        }
        return layerList
    }

    private fun setLoadViewData(
        layerList: LinkedList<LayerItemData>,
        viewSize: Int,
        list: List<SaveViewDataInfo>
    ): LinkedList<ViewItemData> {
        var viewList = LinkedList<ViewItemData>()
        for (i in list.indices) {
            val data = list[i]
            when (list[i].type) {
                0 -> {
                    imageAddViewList(
                        viewList,
                        id = layerList[i].id,
                        bitmap = uriToBitmap(context = context, imageUri = data.img.toUri()),
                        visibility = data.visible
                    )?.let {
                        viewList = it
                    }
                    viewList[i].img.apply {
                        setMatreixData(
                            matrixValue = data.matrixValue,
                            scale = data.scale,
                            degrees = data.rotationDegrees
                        )
                    }
                    viewList[i].img.setImageSaturation(data.saturationValue.toFloat())
                    viewList[i].img.setImageBrightness(data.brightnessValue.toFloat())
                    viewList[i].img.setImageTransparency(data.transparencyValue.toFloat())
                }

                1 -> {
                    viewList = textViewRepositoryImpl.editTextViewAddViewList(
                        viewList,
                        viewSize,
                        layerList[i].id,
                        data.visible
                    )
                    viewList[i].text.apply {
                        setMatrixData(
                            matrixValue = data.matrixValue,
                            scale = data.scale,
                            degrees = data.rotationDegrees
                        )
                        typeface = UtilList.typefaces[data.font]
                        setTextSize(data.textSize.toFloat())
                    }
                    viewList[i].text.setSaturation(data.saturationValue.toFloat())
                    viewList[i].text.setBrightness(data.brightnessValue.toFloat())
                    viewList[i].text.setTransparency(data.transparencyValue.toFloat())
                    viewList[i].text.setText(data.text)
                    viewList[i].text.setTextColor(data.textColor)
                    viewList[i].text.setBackgroundClolor(data.bgColor)
                }
            }
            viewList[i].saturation = data.saturationValue
            viewList[i].brightness = data.brightnessValue
            viewList[i].transparency = data.transparencyValue
        }
        return viewList
    }


    private fun changeViewList(viewList: LinkedList<ViewItemData>, changeId: Int, bitmap: Bitmap) {
        val selectItem = viewList.find { it.id == changeId }
        selectItem?.let {
            val imageView =
                createEditableImageView(context = context, viewId = changeId, bitmap = bitmap)
            it.img = imageView
        }
    }
}