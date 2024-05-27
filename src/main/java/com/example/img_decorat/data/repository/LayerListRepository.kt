package com.example.img_decorat.data.repository

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.net.toUri
import com.example.img_decorat.data.model.dataModels.EmojiDBData
import com.example.img_decorat.data.model.dataModels.EmojiData
import com.example.img_decorat.data.model.dataModels.EmojiList

import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.data.model.dataModels.SaveViewDataInfo
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.utils.Util.bitmapToUri
import com.example.img_decorat.utils.Util.createEditableImageView
import com.example.img_decorat.utils.Util.createEditableTextView
import com.example.img_decorat.utils.Util.resizeBitmap
import com.example.img_decorat.utils.Util.setID
import com.example.img_decorat.utils.Util.stringToTypeface
import com.example.img_decorat.utils.Util.uriToBitmap
import com.example.img_decorat.utils.UtilList
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Collections
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LayerListRepository @Inject constructor(
    @ApplicationContext private val context: Context) {
    var layerList = LinkedList<LayerItemData>()
    var viewList = LinkedList<ViewItemData>()
    lateinit var lastSelectView : ViewItemData

    private var viewSize = 0

    fun setViewSize(size: Int){
        viewSize = size
    }

    fun imgAddList(data: Intent?): LinkedList<LayerItemData>{
        data?.clipData?.let{
            for (i in 0 until it.itemCount) {
                val imageUri: Uri = it.getItemAt(i).uri
                val bitmap = uriToBitmap(context = context, imageUri = imageUri)
                val id = setID()
                bitmap?.let {
                    val resizeBitmap = resizeBitmap(bitmap,viewSize)
                    addLayerList(id, resizeBitmap.first,false)
                    imageAddViewList(id,resizeBitmap.first,false, scale = 1/resizeBitmap.second)
                }
            }
        }
        return layerList
    }

    fun checkedUpdateLayerList(position: Int, checked: Boolean): LinkedList<LayerItemData>{
        val layerItemData: LayerItemData = layerList[position]
        layerItemData.check = checked

        layerList.set(position,layerItemData)
        return layerList
    }

    fun checkedUpdateViewList(position: Int, checked: Boolean): LinkedList<ViewItemData>{
        val targetItem = viewList.find{it.id == layerList[position].id}

        targetItem?.let {
            if(checked){
                it.visible = true
            }else{
                it.visible = false
            }
        }
        return viewList
    }

    fun deleteLayerList(position: Int):LinkedList<LayerItemData>{
        layerList.removeAt(position)
        return layerList
    }

    fun deleteImageViewList(position: Int):LinkedList<ViewItemData>{
        viewList.removeAt(position)
        return viewList
    }

    fun selectLayer(position: Int):LinkedList<LayerItemData>{
        val selectedItem = layerList.find { it.select }

        selectedItem?.let {
            it.select = false
        }

        layerList[position].select = true
        setLastSelectView(layerList[position].id)
        return layerList
    }

    fun selectLastImage(id: Int):LinkedList<LayerItemData>{
        val selectedItem = layerList.find { it.select }
        val findItem = layerList.find { it.id == id }

        selectedItem?.let {
            it.select = false
        }

        findItem?.let {
            it.select = true
        }
        setLastSelectView(id)

        return layerList
    }

    fun swapImageView(fromPos: Int, toPos: Int):LinkedList<ViewItemData>{
        Collections.swap(viewList,fromPos,toPos)
        return viewList
    }

    fun setLastTouchedImage(id: Int): Uri?{
        val image = layerList.find { it.id == id }
        image?.let {
            return bitmapToUri(context = context, it.bitMap)
        }
        return null
    }

    fun splitImageChangeList(uri: Uri): LinkedList<LayerItemData>{
        val bitmap = uriToBitmap(context = context, imageUri = uri)
        val selectedItem = layerList.find { it.select}
        selectedItem?.let {item->
            bitmap?.let {
                item.bitMap = it
                changeViewList(item.id,it)
            }
        }
        return layerList
    }

    fun checkedViewType(id: Int?):Int{
        val selectItem = layerList.find { it.id == id }
        selectItem?.let {
            return it.type
        }
        return -1
    }

    fun checkEditableTextView(id: Int): Boolean{
        val findItem = viewList.find { it.id == id }
        findItem?.let {
            if (it.type == 1){
                return false
            }
        }
        return true
    }

    fun checkLastSelectImage(id: Int):Boolean{
        val selectedItem = layerList.find { it.select }
        selectedItem?.let {
            if(selectedItem.id == id){
                return false
            }else{
                return true
            }
        } ?: run{
            selectLastImage(id)
            return true
        }
    }

    fun editViewSaturation(saturation : Int){
        val selectedItem = layerList.find { it.select }
        selectedItem?.let {item->
            val id = item.id
            val selectView = viewList.find { it.id == id }
            selectView?.let {
                it.saturation = saturation
                when(it.type){
                    0->{
                        it.img.setImageSaturation(saturation.toFloat())
                    }
                    1->{
                        it.text.setSaturation(saturation.toFloat())
                    }
                }
            }
        }
    }

    fun editImageViewBrightness(brightness : Int){
        val selectedItem = layerList.find { it.select }
        selectedItem?.let {item->
            val id = item.id
            val selectImageView = viewList.find { it.id == id }
            selectImageView?.let {
                it.brightness = brightness
                when(it.type){
                    0->{
                        it.img.setImageBrightness(brightness.toFloat())
                    }
                    1->{
                        it.text.setBrightness(brightness.toFloat())
                    }
                }
            }
        }
    }

    fun viewTransparency(alpha : Int){
        val selectedItem = layerList.find { it.select }
        selectedItem?.let {item->
            val id = item.id
            val selectImageView = viewList.find { it.id == id }
            selectImageView?.let {
                it.transparency = alpha
                when(it.type){
                    0->{
                        it.img.setImageTransparency(alpha.toFloat())
                    }
                    1->{
                        it.text.setTransparency(alpha.toFloat())
                    }
                }
            }
        }
    }

    fun checkSaturatio(saturatio: Int?):Int?{
        saturatio?.let {
            if(lastSelectView.saturation != it){
                return lastSelectView.saturation
            }
        }
        return saturatio
    }

    fun checkBrightness(brightness: Int?):Int?{
        brightness?.let {
            if(lastSelectView.brightness != brightness){
                return lastSelectView.brightness
            }
        }
        return brightness
    }

    fun checkTransparency(transparency: Int?):Int?{
        transparency?.let {
            if(lastSelectView.transparency != it){
                return lastSelectView.transparency
            }
        }
        return transparency
    }

    fun emojiAddLayer(bitmap: Bitmap):LinkedList<LayerItemData>{
        val resizeBitmap = resizeBitmap(bitmap,viewSize).first
        val id = setID()
        val layerItemData = LayerItemData(context = context, check = true, id = id, bitMap = resizeBitmap)
        layerList.add(layerItemData)
        imageAddViewList(id,resizeBitmap,true,0.4f)
        return layerList
    }

    fun emojiDataStringToBitmap(list : List<EmojiDBData>): List<EmojiList>{
        val transformationList = mutableListOf<EmojiList>()
        for (i in list){
            val groupName = i.groupName
            val emojiList = mutableListOf<Bitmap>()
            for(j in i.groupList){
                emojiList.add(createBitmapFromEmoji(j))
            }
            transformationList.add(EmojiList(groupName,emojiList))
        }
        return transformationList
    }

    fun emojiDBListClassification(list : List<EmojiData>): List<EmojiDBData>{
        val classification = mutableListOf<EmojiDBData>()
        var groupName = list[0].group
        var emojiBitmapList = mutableListOf<String>()

        for(i in list){
            if(i.group != groupName){
                val emojiGroup  = EmojiDBData(groupName = groupName, groupList = emojiBitmapList)
                classification.add(emojiGroup)
                groupName = i.group
                emojiBitmapList = mutableListOf()
                emojiBitmapList.add(i.character)
            }else{
                emojiBitmapList.add(i.character)
            }
        }
        val emojiGroup  = EmojiDBData(groupName = groupName, groupList = emojiBitmapList)
        classification.add(emojiGroup)
        return classification
    }

    fun editTextViewAddList():LinkedList<LayerItemData>{
        val textId = setID()
        addEditTextViewViewList(textId,"")
        editTextViewAddViewList(textId,true)
        return layerList
    }

    fun editTextViewSetTextColor(idValue: Int, color: Int):LinkedList<LayerItemData>{
        if(lastSelectView.type == 1){
            layerTextViewSetTextColor(id = idValue, color = color)
            lastSelectView.text.apply {
                setTextColor(color)
            }
        }
        return layerList
    }

    fun editTextViewSetBackgroundColor(id: Int, color: Int):LinkedList<LayerItemData>{
        if(lastSelectView.type ==1){
            layerTextViewSetTextBackgroundColor(id = id, color = color)
            lastSelectView.text.setBackgroundClolor(color)
        }
        return layerList
    }

    fun EditTextViewSetTextSize(size: Int){
        if(lastSelectView.type ==1){
            lastSelectView.text.apply {
                setTextSize(size.toFloat())
            }
        }
    }

    fun editTextViewSetFont(id: Int, font: Typeface):LinkedList<LayerItemData>{
        if(lastSelectView.type ==1){
            layerTextViewSetTextFont(id = id, font = font)
            lastSelectView.text.apply {
                typeface = font
            }
        }
        return layerList
    }

    fun layerViewUpdateText(textData: String):LinkedList<LayerItemData>{
        if(lastSelectView.type ==1){
            val selectItem = layerList.find { it.select }
            selectItem?.let {
                it.text.apply {
                    text = textData
                }
            }
        }
        return layerList
    }

    fun loadLayerList(data : SaveViewData):LinkedList<LayerItemData>{
        layerList = LinkedList<LayerItemData>()
        val list = data.data
        setLoadLayerData(list)

        return layerList
    }

    fun loadViewList(data : SaveViewData):LinkedList<ViewItemData>{
        viewList = LinkedList<ViewItemData>()
        val list = data.data
        setLoadViewData(list)
        return viewList
    }

    private fun setLoadLayerData(list : List<SaveViewDataInfo>){
        for (i in list){
            val idValue = setID()
            when(i.type){
                0->{
                    val bitmap = uriToBitmap(context, i.img.toUri())
                    bitmap?.let {
                        addLayerList(id = idValue, bitmap = it, check = i.visible)
                    }
                }
                1->{
                    addEditTextViewViewList(idValue,i.text)
                    layerTextViewSetTextColor(id = idValue, color = i.textColor)
                    layerTextViewSetTextBackgroundColor(id = idValue, color = i.bgColor)
                    layerTextViewSetTextFont(id = idValue, font = UtilList.typefaces[i.font])
                }
            }
        }
    }

    private fun setLoadViewData(list : List<SaveViewDataInfo>){
        for(i in list.indices){
            val data = list[i]
            when(list[i].type){
                0->{
                    imageAddViewList(
                        id = layerList[i].id,
                        bitmap = uriToBitmap(context = context, imageUri = data.img.toUri()),
                        visibility = data.visible
                    )
                    viewList[i].img.apply {
                        setMatreixData(
                            matrixValue = data.matrixValue,
                            scale = data.scale,
                            degrees = data.rotationDegrees)
                    }
                    viewList[i].img.setImageSaturation(data.saturationValue.toFloat())
                    viewList[i].img.setImageBrightness(data.brightnessValue.toFloat())
                    viewList[i].img.setImageTransparency(data.transparencyValue.toFloat())
                }
                1->{
                    editTextViewAddViewList(
                        addId = layerList[i].id,
                        visibility = data.visible
                    )
                    viewList[i].text.apply {
                        setMatrixData(
                            matrixValue = data.matrixValue,
                            scale = data.scale,
                            degrees = data.rotationDegrees)
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

    }

    private fun addLayerList(id : Int, bitmap: Bitmap?, check : Boolean){
        bitmap?.let {
            val layerItemData = LayerItemData(context = context, check = check, id = id, bitMap = it)
            layerList.add(layerItemData)
        }
    }

    private fun imageAddViewList(id : Int, bitmap: Bitmap?, visibility: Boolean, scale : Float = 1.0f){
        bitmap?.let {
            val imageView = createEditableImageView(context= context, viewId = id, bitmap = it, scale = scale)
            val viewData = ViewItemData(context = context, id = imageView.id, visible = visibility)
            viewData.img = imageView
            viewList.add(viewData)
        }
    }

    private fun changeViewList(changeId : Int, bitmap: Bitmap){
        val selectItem = viewList.find { it.id == changeId }
        selectItem?.let {
            val imageView = createEditableImageView(context = context, viewId = changeId, bitmap = bitmap)
            it.img = imageView
        }
    }

    private fun setLastSelectView(id: Int){
        val selectedItem = viewList.find { it.id == id }
        selectedItem?.let {
            lastSelectView = it
        }
    }

    private fun createBitmapFromEmoji(emoji: String): Bitmap {
        val paint = Paint()
        paint.textSize = 200f
        paint.isAntiAlias = true
        paint.textAlign = Paint.Align.CENTER

        val baseline = - paint.ascent()
        val width = (paint.measureText(emoji) + 0.5f).toInt()
        val height = (baseline + paint.descent() + 0.5f).toInt()

        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawText(emoji, (width / 2).toFloat(), baseline, paint)

        return image
    }

    private fun addEditTextViewViewList(textId: Int, textValue: String){
        var layerItemData = LayerItemData(context = context, check = true, id = textId, type = 1)
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
    }

    private fun editTextViewAddViewList(addId : Int, visibility: Boolean){
        val editView = createEditableTextView(
            context = context,
            viewId = addId,
            withSize = viewSize,
            heightSize = viewSize)
        val viewData = ViewItemData(context = context, id = editView.id, visible = visibility, type = 1)
        viewData.text = editView
        viewList.add(viewData)
    }

    private fun layerTextViewSetTextColor(id: Int, color: Int){
        val selectItem = layerList.find { it.id == id }
        selectItem?.let {
            it.text.apply {
                setTextColor(color)
            }
        }
    }

    private fun layerTextViewSetTextBackgroundColor(id: Int, color: Int){
        val selectItem = layerList.find { it.id == id }
        selectItem?.let {
            it.text.apply {
                setBackgroundColor(color)
            }
        }
    }

    private fun layerTextViewSetTextFont(id: Int, font: Typeface){
        val selectItem = layerList.find { it.id == id }
        selectItem?.let {
            it.text.apply {
                typeface = font
            }
        }
    }
}