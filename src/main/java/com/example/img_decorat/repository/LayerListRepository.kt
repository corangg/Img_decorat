package com.example.img_decorat.repository

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData
import com.example.img_decorat.dataModels.ImageViewData
import com.example.img_decorat.dataModels.ImgLayerData
import com.example.img_decorat.ui.view.EditableImageView
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LayerListRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageDataRepository: ImageDataRepository) {

    var layerList = LinkedList<ImgLayerData>()
    val liveLayerList : MutableLiveData<LinkedList<ImgLayerData>> = MutableLiveData(LinkedList<ImgLayerData>())

    var imageViewList = LinkedList<ImageViewData>()
    val liveImageViewList : MutableLiveData<LinkedList<ImageViewData>> = MutableLiveData()

    fun uriToBitmap(context: Context, imageUri: Uri): Bitmap? {
        context.contentResolver.openInputStream(imageUri).use { inputStream ->
            return BitmapFactory.decodeStream(inputStream)
        }
    }
    fun setImgLayerList(data: Intent?){
        data?.clipData?.let{ clipData ->
            for (i in 0 until clipData.itemCount) {
                val imageUri: Uri = clipData.getItemAt(i).uri
                val bitmap = uriToBitmap(context,imageUri)
                val id = imageDataRepository.setID()
                if(bitmap != null){
                    val layerData = ImgLayerData(bitmap,false,id)
                    layerList.add(layerData)
                    addImageView(id,bitmap)
                }
            }
        } ?: data?.data?.let { uri ->
            val bitmap = uriToBitmap(context,uri)
            val id = imageDataRepository.setID()
            if(bitmap != null){
                val layerData = ImgLayerData(bitmap,false,id)
                layerList.add(layerData)
                addImageView(id,bitmap)
            }
        }
        liveLayerList.value = layerList
    }

    fun addImageView(addId : Int, bitmap: Bitmap){
        val imageView = EditableImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            id = addId
            setImageBitmap(bitmap)
        }
        imageViewList.add(ImageViewData(imageView,false))
    }








   /* fun setImgLayerList(layerList:LinkedList<ImgLayerData>,imageList:LinkedList<ImageViewData>, data: Intent?): Pair<LinkedList<ImgLayerData>,LinkedList<ImageViewData>>{
        val imgLayerList = layerList
        val imgViewList = imageList
        data?.clipData?.let{ clipData ->
            for (i in 0 until clipData.itemCount) {
                val imageUri: Uri = clipData.getItemAt(i).uri
                val bitmap = uriToBitmap(context,imageUri)
                val id = imageDataRepository.setID()
                if(bitmap != null){
                    val layerData = ImgLayerData(bitmap,false,id)
                    imgLayerList.add(layerData)
                    imgViewList.add(addImageView(id,bitmap))
                }
            }
        } ?: data?.data?.let { uri ->
            val bitmap = uriToBitmap(context,uri)
            val id = imageDataRepository.setID()
            if(bitmap != null){
                val layerData = ImgLayerData(bitmap,false,id)
                imgLayerList.add(layerData)
                imgViewList.add(addImageView(id,bitmap))
            }
        }
        return Pair(imgLayerList, imgViewList)
    }

    fun addImageView(addId : Int, bitmap: Bitmap): ImageViewData{
        val imageView = EditableImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            id = addId
            setImageBitmap(bitmap)
        }
        return ImageViewData(imageView,false)
    }*/
}