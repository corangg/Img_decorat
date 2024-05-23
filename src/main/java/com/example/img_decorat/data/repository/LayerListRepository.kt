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
import com.example.img_decorat.data.model.dataModels.EmojiData
import com.example.img_decorat.data.model.dataModels.EmojiList

import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.ui.view.TextImageView
import com.example.img_decorat.utils.Util.bitmapToUri
import com.example.img_decorat.utils.Util.createEditableImageView
import com.example.img_decorat.utils.Util.resizeBitmap
import com.example.img_decorat.utils.Util.uriToBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Collections
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LayerListRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageDataRepository: ImageDataRepository
) {
    var layerList = LinkedList<LayerItemData>()
    val viewList = LinkedList<ViewItemData>()
    lateinit var lastSelectView : ViewItemData

    fun imgAddList(data: Intent?, viewSize: Int): LinkedList<LayerItemData>{
        data?.clipData?.let{
            for (i in 0 until it.itemCount) {
                val imageUri: Uri = it.getItemAt(i).uri
                val bitmap = uriToBitmap(context = context, imageUri = imageUri)
                val id = imageDataRepository.setID()
                bitmap?.let {
                    val resizeBitmap = resizeBitmap(bitmap,viewSize)
                    addLayerList(id,resizeBitmap.first)
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

    fun emojiAddLayer(bitmap: Bitmap, size:Int):LinkedList<LayerItemData>{
        val resizeBitmap = resizeBitmap(bitmap,size).first
        val id = imageDataRepository.setID()
        val layerItemData = LayerItemData(context = context, check = true, id = id, bitMap = resizeBitmap)
        layerList.add(layerItemData)
        imageAddViewList(id,resizeBitmap,true,0.4f)
        return layerList
    }

    fun emojiListClassification(list : List<EmojiData>): List<EmojiList>{
        val classification = mutableListOf<EmojiList>()
        var groupName = list[0].group
        var emojiBitmapList = mutableListOf<Bitmap>()

        for(i in list){
            if(i.group != groupName){
                val emojiGroup  = EmojiList(groupName = groupName, groupList = emojiBitmapList)
                classification.add(emojiGroup)
                groupName = i.group
                emojiBitmapList = mutableListOf()
                emojiBitmapList.add(createBitmapFromEmoji(i.character))
            }else{
                emojiBitmapList.add(createBitmapFromEmoji(i.character))
            }
        }
        val emojiGroup  = EmojiList(groupName = groupName, groupList = emojiBitmapList)
        classification.add(emojiGroup)
        return classification
    }

    /*fun emojiDBListClassification(list : List<EmojiData>): List<EmojiListData>{
        val classification = mutableListOf<EmojiListData>()
        var groupName = list[0].group
        var emojiBitmapList = mutableListOf<String>()

        for(i in list){
            if(i.group != groupName){
                val emojiGroup  = EmojiListData(groupName = groupName, groupList = emojiBitmapList)
                classification.add(emojiGroup)
                groupName = i.group
                emojiBitmapList = mutableListOf()
                emojiBitmapList.add(i.character)
            }else{
                emojiBitmapList.add(i.character)
            }
        }
        val emojiGroup  = EmojiListData(groupName = groupName, groupList = emojiBitmapList)
        classification.add(emojiGroup)
        return classification
    }*/

    fun editTextViewAddList(viewSize: Int):LinkedList<LayerItemData>{
        val textId = imageDataRepository.setID()
        var layerItemData = LayerItemData(context = context, check = true, id = textId, type = 1)
        layerItemData.text = TextView(context).apply {
            id = textId
            setBackgroundColor(Color.TRANSPARENT)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        }
        layerList.add(layerItemData)
        editTextViewAddViewList(textId,viewSize,true)
        return layerList
    }

    fun editTextViewSetTextColor(color: Int):LinkedList<LayerItemData>{
        if(lastSelectView.type == 1){
            val selectItem = layerList.find { it.select }
            selectItem?.let {
                it.text.apply {
                    setTextColor(color)
                }
            }
            lastSelectView.text.apply {
                setTextColor(color)
            }
        }
        return layerList
    }

    fun editTextViewSetBackgroundColor(color: Int):LinkedList<LayerItemData>{
        if(lastSelectView.type ==1){
            val selectItem = layerList.find { it.select }
            selectItem?.let {
                it.text.apply {
                    setBackgroundColor(color)
                }
            }
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

    fun EditTextViewSetFont(font: Typeface):LinkedList<LayerItemData>{
        if(lastSelectView.type ==1){
            val selectItem = layerList.find { it.select }
            selectItem?.let {
                it.text.apply {
                    typeface = font
                }
            }
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

    private fun addLayerList(id : Int, bitmap: Bitmap){
        val layerItemData = LayerItemData(context = context, check = false, id = id, bitMap = bitmap)
        layerList.add(layerItemData)
    }

    private fun imageAddViewList(id : Int, bitmap: Bitmap, visibility: Boolean, scale : Float = 1.0f){
        val imageView = createEditableImageView(context= context, viewId = id, bitmap = bitmap, scale = scale)
        val viewData = ViewItemData(context = context, id = imageView.id, visible = visibility)
        viewData.img = imageView
        viewList.add(viewData)
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

    private fun editTextViewAddViewList(addId : Int, viewSize: Int, visibility: Boolean){
        val editView = TextImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                viewSize,
                viewSize
            )
            id = addId
            setTextSize(24f)
        }
        var viewData = ViewItemData(context = context, id = editView.id, visible = visibility, type = 1)
        viewData.text = editView
        viewList.add(viewData)
    }
}