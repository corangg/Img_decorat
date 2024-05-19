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
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import com.example.img_decorat.dataModels.Font
import com.example.img_decorat.dataModels.ImageViewData
import com.example.img_decorat.dataModels.ImgLayerData
import com.example.img_decorat.dataModels.ListData
import com.example.img_decorat.ui.view.EditableImageView
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
    var imageViewList = LinkedList<ImageViewData>()
    lateinit var lastSelectViewData : ImageViewData

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
        val selectItem = imageViewList.find { it.img.id == changeId }
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
        val layerData = ImgLayerData(bitmap,false,id)
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
        imageViewList.add(ImageViewData(imageView,visibility))
    }

    fun updateLayerListChecked(position: Int, checked: Boolean): LinkedList<ImgLayerData>{
        var layerData: ImgLayerData = layerList[position]
        layerData.check = checked

        layerList.set(position,layerData)
        return layerList
    }

    fun updateImageViewListChecked(position: Int, checked: Boolean): LinkedList<ImageViewData>{
        val checkedId = layerList[position].id
        val targetItem = imageViewList.find{it.img.id == checkedId}

        targetItem?.let {
            if(checked){
                targetItem.visible = true
            }else{
                targetItem.visible = false
            }
        }

        return imageViewList
    }

    fun deleteLayerList(position: Int):LinkedList<ImgLayerData>{
        layerList.removeAt(position)
        return layerList
    }

    fun deleteImageViewList(position: Int):LinkedList<ImageViewData>{
        imageViewList.removeAt(position)
        return imageViewList
    }

    fun createTransparentBitmap(width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.TRANSPARENT)
        }
    }

    fun addLayer():LinkedList<ImgLayerData>{
        val bitmap = createTransparentBitmap(1024,1024)//크기 임시임
        val id = imageDataRepository.setID()
        val layerData = ImgLayerData(bitmap,false,id)
        layerList.add(layerData)
        addImageViewList(id,bitmap,false)
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
        val selectedItem = imageViewList.find { it.img.id == id }
        selectedItem?.let {
            lastSelectViewData = it
        }
    }

    fun swapImageView(fromPos: Int, toPos: Int):LinkedList<ImageViewData>{
        Collections.swap(imageViewList,fromPos,toPos)
        return imageViewList
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
            val selectImageView = imageViewList.find { it.img.id == id }
            selectImageView?.let {
                selectImageView.saturation = saturation
                selectImageView.img.setImageSaturation(saturation.toFloat())
            }
        }
    }

    fun checkSaturatio(saturatio: Int?):Int{
        if(lastSelectViewData.saturation != saturatio){
            return lastSelectViewData.saturation
        }else{
            return saturatio
        }
    }

    fun editImageViewBrightness(brightness : Int){
        val selectedItem = layerList.find { it.select }
        selectedItem?.let {
            val id = selectedItem.id
            val selectImageView = imageViewList.find { it.img.id == id }
            selectImageView?.let {
                selectImageView.brightness = brightness
                selectImageView.img.setImageBrightness(brightness.toFloat())
            }
        }
    }

    fun checkBrightness(brightness: Int?):Int{
        if(lastSelectViewData.brightness != brightness){
            return lastSelectViewData.brightness
        }else{
            return brightness
        }
    }

    fun editImageViewTransparency(alpha : Int){
        val selectedItem = layerList.find { it.select }
        selectedItem?.let {
            val id = selectedItem.id
            val selectImageView = imageViewList.find { it.img.id == id }
            selectImageView?.let {
                selectImageView.transparency = alpha
                selectImageView.img.setImageTransparency(alpha.toFloat())
            }
        }
    }

    fun checkTransparency(transparency: Int?):Int{
        if(lastSelectViewData.transparency != transparency){
            return lastSelectViewData.transparency
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
        val layerData = ImgLayerData(resizeBitmap,true,id)
        layerList.add(layerData)
        addImageViewList(id,resizeBitmap,true,0.4f)
        return layerList
    }

}