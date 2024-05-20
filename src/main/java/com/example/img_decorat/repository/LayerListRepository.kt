package com.example.img_decorat.repository

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.FileProvider

import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.ui.view.EditableImageView
import com.example.img_decorat.ui.view.TextImageView
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Collections
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LayerListRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageDataRepository: ImageDataRepository) {

    var layerList = LinkedList<LayerItemData>()
    val viewList = LinkedList<ViewItemData>()
    lateinit var lastSelectView : ViewItemData

    fun uriToBitmap(imageUri: Uri): Bitmap? {
        context.contentResolver.openInputStream(imageUri).use { inputStream ->
            return BitmapFactory.decodeStream(inputStream)
        }
    }

    fun Context.bitmapToUri(bitmap: Bitmap): Uri? {
        val file = File(cacheDir, "${System.currentTimeMillis()}.png")
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            FileProvider.getUriForFile(this, "${packageName}.provider", file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun setImgLayerList(data: Intent?,viewSize: Int):LinkedList<LayerItemData>{
        data?.clipData?.let{ clipData ->
            for (i in 0 until clipData.itemCount) {
                val imageUri: Uri = clipData.getItemAt(i).uri
                val bitmap = uriToBitmap(imageUri)
                val id = imageDataRepository.setID()
                bitmap?.let {
                    val resizeBitmap = resizeBitmap(bitmap,viewSize)
                    addLayerList(id,resizeBitmap.first)
                    addImageViewList(id,resizeBitmap.first,false, scale = 1/resizeBitmap.second)
                }
            }
        } ?: data?.data?.let { uri ->
            val bitmap = uriToBitmap(uri)
            val id = imageDataRepository.setID()
            bitmap?.let {
                val resizeBitmap = resizeBitmap(bitmap,viewSize)
                addLayerList(id,resizeBitmap.first)
                addImageViewList(id,resizeBitmap.first,false, scale = 1/resizeBitmap.second)
            }
        }
        return layerList
    }

    fun addSplitImage(uri: Uri):LinkedList<LayerItemData>{
        val bitmap = uriToBitmap(uri)
        val selectItem = layerList.find { it.select}
        selectItem?.let {
            bitmap?.let {
                selectItem.bitMap = bitmap
                changeImageViewList(selectItem.id,bitmap)
            }
        }
        return layerList
    }

    fun changeImageViewList(changeId : Int, bitmap: Bitmap){
        //val selectItem = imageViewList.find { it.img.id == changeId }
        val selectItem = viewList.find { it.id == changeId }
        selectItem?.let {
            val imageView = EditableImageView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                id = changeId
                setImageBitmap(bitmap)
            }
            selectItem.img=imageView
        }
    }

    fun addLayerList(id : Int, bitmap: Bitmap){
        val layerItemData = LayerItemData(context = context, check = false, id = id, bitMap = bitmap)
        layerList.add(layerItemData)
    }

    fun addImageViewList(addId : Int, bitmap: Bitmap, visibility: Boolean, scale : Float = 1.0f){
        val imageView = EditableImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            id = addId
            setImageBitmap(bitmap)
        }
        imageView.setImageScale(scale)
        var viewData = ViewItemData(context = context, id = imageView.id, visible = visibility)
        viewData.img = imageView
        viewList.add(viewData)
    }

    fun addTextViewList(addId : Int,viewSize: Int, visibility: Boolean, scale : Float = 1.0f){
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



    fun updateLayerListChecked(position: Int, checked: Boolean): LinkedList<LayerItemData>{
        var layerItemData: LayerItemData = layerList[position]
        layerItemData.check = checked

        layerList.set(position,layerItemData)
        return layerList
    }

    fun updateImageViewListChecked(position: Int, checked: Boolean): LinkedList<ViewItemData>{
        val checkedId = layerList[position].id
        val targetItem = viewList.find{it.id == checkedId}

        targetItem?.let {
            if(checked){
                targetItem.visible = true
            }else{
                targetItem.visible = false
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

    fun createTransparentBitmap(width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.TRANSPARENT)
        }
    }

    fun addLayer():LinkedList<LayerItemData>{//이 기능 굳이 필요한가 싶음
        val bitmap = createTransparentBitmap(1024,1024)//크기 임시임
        val id = imageDataRepository.setID()
        val layerItemData = LayerItemData(context = context, check = false, id = id, bitMap = bitmap)
        layerList.add(layerItemData)
        addImageViewList(id,bitmap,false)
        return layerList
    }

    fun addTextLayer(viewSize: Int):LinkedList<LayerItemData>{
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
        addTextViewList(textId,viewSize,true)
        return layerList
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

        selectedItem?.let {
            it.select = false
        }

        val findItem = layerList.find { it.id == id }
        findItem?.let {
            it.select = true
        }

        return layerList
    }

    fun setLastSelectView(id: Int){
        val selectedItem = viewList.find { it.id == id }
        selectedItem?.let {
            lastSelectView = it
        }
    }

    fun swapImageView(fromPos: Int, toPos: Int):LinkedList<ViewItemData>{
        Collections.swap(viewList,fromPos,toPos)
        return viewList
    }

    fun setLastTouchedImage(id: Int): Uri{
        val image = layerList.find { it.id == id }
        return context.bitmapToUri(image!!.bitMap)!!//image!!.bitMap//null가능성 없는거 같긴한데...
    }

    fun splitBitmaptoUri(bitmap: Bitmap) :Uri{
        return context.bitmapToUri(bitmap)!!
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

    fun editImageViewSaturation(saturation : Int){
        val selectedItem = layerList.find { it.select }
        selectedItem?.let {
            val id = selectedItem.id
            val selectImageView = viewList.find { it.id == id }
            selectImageView?.let {
                selectImageView.saturation = saturation
                when(selectImageView.type){
                    0->{
                        selectImageView.img.setImageSaturation(saturation.toFloat())
                    }
                    1->{
                        selectImageView.text.setSaturation(saturation.toFloat())
                    }
                }
            }
        }
    }

    fun checkSaturatio(saturatio: Int?):Int{
        if(lastSelectView.saturation != saturatio){
            return lastSelectView.saturation
        }else{
            return saturatio
        }
    }

    fun editImageViewBrightness(brightness : Int){
        val selectedItem = layerList.find { it.select }
        selectedItem?.let {
            val id = selectedItem.id
            val selectImageView = viewList.find { it.id == id }
            selectImageView?.let {
                selectImageView.brightness = brightness
                when(selectImageView.type){
                    0->{
                        selectImageView.img.setImageBrightness(brightness.toFloat())
                    }
                    1->{
                        selectImageView.text.setBrightness(brightness.toFloat())
                    }
                }

            }
        }
    }

    fun checkBrightness(brightness: Int?):Int{
        if(lastSelectView.brightness != brightness){
            return lastSelectView.brightness
        }else{
            return brightness
        }
    }

    fun viewTransparency(alpha : Int){
        val selectedItem = layerList.find { it.select }
        selectedItem?.let {
            val id = selectedItem.id
            val selectImageView = viewList.find { it.id == id }
            selectImageView?.let {
                selectImageView.transparency = alpha
                when(selectImageView.type){
                    0->{
                        selectImageView.img.setImageTransparency(alpha.toFloat())
                    }
                    1->{
                        selectImageView.text.setTransparency(alpha.toFloat())
                    }
                }
            }
        }
    }

    fun checkTransparency(transparency: Int?):Int{
        if(lastSelectView.transparency != transparency){
            return lastSelectView.transparency
        }else{
            return transparency
        }
    }

    fun resizeBitmap(bitmap: Bitmap,size: Int): Pair<Bitmap,Float>{
        if(bitmap.height > bitmap.width){
            val scale = bitmap.height.toFloat()/bitmap.width.toFloat()
            val height = scale*size
            return Pair(Bitmap.createScaledBitmap(bitmap,size,height.toInt(),true),scale)

        }else{
            val scale = bitmap.width.toFloat()/bitmap.height.toFloat()
            val width = scale*size
        return Pair(Bitmap.createScaledBitmap(bitmap,width.toInt(),size,true),scale)
        }
    }

    fun addEmojiLayer(bitmap: Bitmap,size:Int):LinkedList<LayerItemData>{
        val resizeBitmap = resizeBitmap(bitmap,size).first
        val id = imageDataRepository.setID()
        val layerItemData = LayerItemData(context = context, check = true, id = id)
        layerList.add(layerItemData)
        addImageViewList(id,resizeBitmap,true,0.4f)
        return layerList
    }

    fun divisionViewType(id: Int?):Int{
        val selectItem = layerList.find { it.id == id }
        var type = -1
        selectItem?.let {
            type = it.type
        }
        return type
    }

    fun setEditTextViewTextColor(color: Int):LinkedList<LayerItemData>{
        if(lastSelectView.type ==1){
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

    fun setEditTextViewBackgroundColor(color: Int):LinkedList<LayerItemData>{
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

    fun setEditTextVIewTextSize(size: Int){
        if(lastSelectView.type ==1){
            lastSelectView.text.apply {
                setTextSize(size.toFloat())
            }
        }
    }

    fun setEditTextViewTextFont(font: Typeface):LinkedList<LayerItemData>{
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

    fun updateLayerViewText(textData: String):LinkedList<LayerItemData>{
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
}