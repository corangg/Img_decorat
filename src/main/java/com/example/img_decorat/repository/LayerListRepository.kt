package com.example.img_decorat.repository

import android.app.Application
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
import androidx.lifecycle.MutableLiveData
import com.example.img_decorat.dataModels.AEditText

import com.example.img_decorat.dataModels.ImgLayerData
import com.example.img_decorat.dataModels.ListData
import com.example.img_decorat.dataModels.ViewListData
import com.example.img_decorat.ui.view.EditableImageView
import com.example.img_decorat.ui.view.TextImageView
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Collections
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LayerListRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageDataRepository: ImageDataRepository) {

    var layerList = LinkedList<ImgLayerData>()
    //var imageViewList = LinkedList<ImageViewData>()
    val viewList = LinkedList<ViewListData>()
    //lateinit var lastSelectViewData : ImageViewData
    lateinit var lastSelectView : ViewListData

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

    fun setImgLayerList(data: Intent?,viewSize: Int):LinkedList<ImgLayerData>{
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

    fun addSplitImage(uri: Uri):LinkedList<ImgLayerData>{
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
        val layerData = ImgLayerData(bitmap,context,false,id)
        layerList.add(layerData)
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
        //imageViewList.add(ImageViewData(imageView,visibility))
        var viewData = ViewListData(context,imageView.id, visible = visibility)
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
        }
        var viewData = ViewListData(context,editView.id, visible = visibility, type = 1)
        viewData.text = editView
        viewList.add(viewData)
    }



    fun updateLayerListChecked(position: Int, checked: Boolean): LinkedList<ImgLayerData>{
        var layerData: ImgLayerData = layerList[position]
        layerData.check = checked

        layerList.set(position,layerData)
        return layerList
    }

    fun updateImageViewListChecked(position: Int, checked: Boolean): LinkedList<ViewListData>{
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

    fun deleteLayerList(position: Int):LinkedList<ImgLayerData>{
        layerList.removeAt(position)
        return layerList
    }

    fun deleteImageViewList(position: Int):LinkedList<ViewListData>{
        viewList.removeAt(position)
        return viewList
    }

    fun createTransparentBitmap(width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.TRANSPARENT)
        }
    }

    fun createTextView(width: Int, height: Int){
        //return TextView(context)
    }

    fun addLayer():LinkedList<ImgLayerData>{
        val bitmap = createTransparentBitmap(1024,1024)//크기 임시임
        val id = imageDataRepository.setID()
        val layerData = ImgLayerData(bitmap,context,false,id)
        layerList.add(layerData)
        addImageViewList(id,bitmap,false)
        return layerList
    }

    fun addTextLayer(viewSize: Int):LinkedList<ImgLayerData>{
        val textId = imageDataRepository.setID()
        var layerData = ImgLayerData(context = context, check = false, id = textId, type = 1)
        layerData.text = TextView(context).apply {
            id = textId
            setBackgroundColor(Color.TRANSPARENT)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        }
        layerList.add(layerData)
        addTextViewList(textId,viewSize,true)
        return layerList
    }

    fun selectLayer(position: Int):LinkedList<ImgLayerData>{
        val selectedItem = layerList.find { it.select }

        selectedItem?.let {
            it.select = false
            }

        layerList[position].select = true
        setLastSelectView(layerList[position].id)

        return layerList
    }

    fun selectLastImage(id: Int):LinkedList<ImgLayerData>{
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

    fun swapImageView(fromPos: Int, toPos: Int):LinkedList<ViewListData>{
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
                selectImageView.img.setImageSaturation(saturation.toFloat())
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
                selectImageView.img.setImageBrightness(brightness.toFloat())
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

    fun editImageViewTransparency(alpha : Int){
        val selectedItem = layerList.find { it.select }
        selectedItem?.let {
            val id = selectedItem.id
            val selectImageView = viewList.find { it.id == id }
            selectImageView?.let {
                selectImageView.transparency = alpha
                selectImageView.img.setImageTransparency(alpha.toFloat())
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

    fun addEmojiLayer(bitmap: Bitmap,size:Int):LinkedList<ImgLayerData>{
        val resizeBitmap = resizeBitmap(bitmap,size).first
        val id = imageDataRepository.setID()
        val layerData = ImgLayerData(resizeBitmap,context,true,id)
        layerList.add(layerData)
        addImageViewList(id,resizeBitmap,true,0.4f)
        return layerList
    }


    /*fun addImageViewList(addId : Int, bitmap: Bitmap, visibility: Boolean, scale : Float = 1.0f){
        val imageView = EditableImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            id = addId
            setImageBitmap(bitmap)
        }
        imageView.setImageScale(scale)
        //imageViewList.add(ImageViewData(imageView,visibility))
        var viewData = ViewListData(context,imageView.id, visible = visibility)
        viewData.img = imageView
        viewList.add(viewData)
    }*/

}