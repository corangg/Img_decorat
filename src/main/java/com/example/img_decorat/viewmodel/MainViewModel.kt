package com.example.img_decorat.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.core.view.ViewCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.img_decorat.ImgLayerData
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application) : AndroidViewModel(application){
    val selectImg : MutableLiveData<Unit> = MutableLiveData()
    val imgTitle : MutableLiveData<String> = MutableLiveData("New_Image")
    val openGalleryEvent : MutableLiveData<Unit> = MutableLiveData()
    val openMenuEvent : MutableLiveData<Boolean> = MutableLiveData()


    var imagesList = LinkedList<ImgLayerData>()
    val layerList : MutableLiveData<LinkedList<ImgLayerData>> = MutableLiveData(LinkedList<ImgLayerData>())//바로 적용안되면 라이브데이터로 바꿔야함


    fun openGallery(){
        openGalleryEvent.value = Unit
    }

    fun moreMenu(){
        if(openMenuEvent.value == true){
            openMenuEvent.value = false
        }else{
            openMenuEvent.value = true
        }
    }

    fun closeMenu(){
        openMenuEvent.value = false
    }

    fun setID() : Int{
        val id =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                View.generateViewId()
            } else {
                ViewCompat.generateViewId()
            }
        return id
    }

    fun setImgLayerList(data: Intent?){
        data?.clipData?.let{ clipData ->
            for (i in 0 until clipData.itemCount) {
                val imageUri: Uri = clipData.getItemAt(i).uri
                val bitmap = uriToBitmap(getApplication<Application>().applicationContext,imageUri)
                if(bitmap != null){
                    val layerData = ImgLayerData(bitmap,false,setID())
                    imagesList.add(layerData)
                }
            }
        } ?: data?.data?.let { uri ->
            val bitmap = uriToBitmap(getApplication<Application>().applicationContext,uri)
            if(bitmap != null){
                val layerData = ImgLayerData(bitmap,false,setID())
                imagesList.add(layerData)
            }
        }
        layerList.value = imagesList
    }

    fun updateChecked(position: Int, checked: Boolean){
        if(imagesList.size > position){
            var layerData: ImgLayerData = imagesList[position]
            layerData.check = checked

            imagesList.set(position,layerData)

            layerList.value = imagesList
        }
    }

    fun deleteLayer(position: Int){
        if(imagesList.size > position){
            imagesList.removeAt(position)
            layerList.value = imagesList
        }
    }

    fun addLayer(){
        val bitmap = createTransparentBitmap(1024,1024)
        val layerData = ImgLayerData(bitmap,false,setID())
        imagesList.add(layerData)
        layerList.value = imagesList
    }


    fun uriToBitmap(context: Context, imageUri: Uri): Bitmap? {
        context.contentResolver.openInputStream(imageUri).use { inputStream ->
            return BitmapFactory.decodeStream(inputStream)
        }
    }

    fun createTransparentBitmap(width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.TRANSPARENT)
        }
    }

    fun selectLayer(position: Int){
        if(imagesList.size > position){
            val selectedItem = imagesList.find { it.select }

            selectedItem?.let {
                it.select = false }

            imagesList[position].select = true
            layerList.value = imagesList
        }
    }

}