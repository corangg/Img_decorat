package com.example.img_decorat.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.img_decorat.dataModels.ImageViewData
import com.example.img_decorat.dataModels.ImgLayerData
import com.example.img_decorat.R
import com.example.img_decorat.repository.RetrofitApi
import com.example.img_decorat.utils.Util
import com.example.img_decorat.ui.view.EditableImageView
import com.example.img_decorat.dataModels.UnsplashData
import com.example.img_decorat.repository.ImageDataRepository
import com.example.img_decorat.repository.LayerListRepository
import com.example.img_decorat.utils.ColorList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Collections
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val imageDataRepository: ImageDataRepository,
    private val layerListRepository: LayerListRepository) : AndroidViewModel(application){
    var screenWith : Int = 0

    val imgTitle : MutableLiveData<String> = MutableLiveData("New_Image")
    val openGalleryEvent : MutableLiveData<Unit> = MutableLiveData()
    val openMenuEvent : MutableLiveData<Boolean> = MutableLiveData()

    val selectNavigationItem : MutableLiveData<Int> = MutableLiveData(0)
    val selectbackgroundMenu: MutableLiveData<Int> = MutableLiveData(0)


    var layerList = LinkedList<ImgLayerData>()
    //val liveLayerList : MutableLiveData<LinkedList<ImgLayerData>> = MutableLiveData(LinkedList<ImgLayerData>())
    val liveLayerList : MutableLiveData<LinkedList<ImgLayerData>> = layerListRepository.liveLayerList

    var imageViewList = LinkedList<ImageViewData>()
    //val liveImageViewList : MutableLiveData<LinkedList<ImageViewData>> = MutableLiveData()
    val liveImageViewList : MutableLiveData<LinkedList<ImageViewData>> = layerListRepository.liveImageViewList

    val lastTouchedImageId : MutableLiveData<Int> = MutableLiveData(-1)


    fun bottomNavigationItemSelected(item : MenuItem):Boolean{
        when(item.itemId){
            R.id.navigation_background->{
                selectNavigationItem.value = 0
                return true
            }
            R.id.navigation_split->{
                selectNavigationItem.value = 1
                return true
            }
            R.id.navigation_hue->{
                selectNavigationItem.value = 2
                return true
            }
            R.id.navigation_sticker->{
                selectNavigationItem.value = 3
                return true
            }
            R.id.navigation_text->{
                selectNavigationItem.value = 4
                return true
            }
            R.id.scale_item->{
                selectbackgroundMenu.value = 0
                return true
            }
            R.id.paint_item->{
                selectbackgroundMenu.value = 1
                return true
            }
            R.id.image_item->{
                selectbackgroundMenu.value = 2
                return true
            }
        }
        return false
    }

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





    fun uriToBitmap(context: Context, imageUri: Uri): Bitmap? {
        context.contentResolver.openInputStream(imageUri).use { inputStream ->
            return BitmapFactory.decodeStream(inputStream)
        }
    }

    fun setImgLayerList(data: Intent?){
       /* data?.clipData?.let{ clipData ->
            for (i in 0 until clipData.itemCount) {
                val imageUri: Uri = clipData.getItemAt(i).uri
                val bitmap = uriToBitmap(getApplication<Application>().applicationContext,imageUri)
                val id = imageDataRepository.setID()
                if(bitmap != null){
                    val layerData = ImgLayerData(bitmap,false,id)
                    layerList.add(layerData)
                    addImageView(id,bitmap)
                }
            }
        } ?: data?.data?.let { uri ->
            val bitmap = uriToBitmap(getApplication<Application>().applicationContext,uri)
            val id = imageDataRepository.setID()
            if(bitmap != null){
                val layerData = ImgLayerData(bitmap,false,id)
                layerList.add(layerData)
                addImageView(id,bitmap)
            }
        }*/
        layerListRepository.setImgLayerList(data)

        liveLayerList.value
        liveImageViewList.value
        true

        /* val pairList = layerListRepository.setImgLayerList(layerList,imageViewList,data)
        layerList = pairList.first
        imageViewList = pairList.second*/
        //liveLayerList.value = layerList
    }

    fun addImageView(addId : Int, bitmap: Bitmap){
        val imageView = EditableImageView(getApplication<Application>().applicationContext).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            id = addId
            setImageBitmap(bitmap)
        }
        imageViewList.add(ImageViewData(imageView,false))
    }






    fun updateChecked(position: Int, checked: Boolean){
        if(layerList.size > position){
            var layerData: ImgLayerData = layerList[position]
            layerData.check = checked

            layerList.set(position,layerData)

            liveLayerList.value = layerList
            if(checked){
                checkedLayer(position)
            }else{
                unCheckedLayer(position)
            }
        }
    }

    fun deleteLayer(position: Int){
        if(layerList.size > position){
            layerList.removeAt(position)
            liveLayerList.value = layerList
            imageViewList.removeAt(position)
            liveImageViewList.value = imageViewList
        }
    }

    fun addLayer(){
        val bitmap = createTransparentBitmap(1024,1024)//크기 임시임
        val id = imageDataRepository.setID()
        val layerData = ImgLayerData(bitmap,false,id)
        layerList.add(layerData)
        liveLayerList.value = layerList
        addImageView(id,bitmap)
    }




    fun createTransparentBitmap(width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.TRANSPARENT)
        }
    }

    fun selectLayer(position: Int){
        if(layerList.size > position){
            val selectedItem = layerList.find { it.select }

            selectedItem?.let {
                it.select = false }

            layerList[position].select = true
            liveLayerList.value = layerList
            lastTouchedImageId.value = layerList[position].id
        }
    }

    fun selectLastImage(id: Int){
        val selectedItem = layerList.find { it.select }

        selectedItem?.let {
            it.select = false }

        val findItem = layerList.find { it.id == id }
        findItem?.let {
            it.select = true
        }
        liveLayerList.value = layerList
        lastTouchedImageId.value = id
    }

    fun checkedLayer(position: Int){
        val checkedId = layerList[position].id
        val targetItem = imageViewList.find{it.img.id == checkedId}

        if(targetItem != null){
            targetItem.visible = true
            liveImageViewList.value = imageViewList
        }
    }

    fun unCheckedLayer(position: Int){
        val checkedId = layerList[position].id
        val targetItem = imageViewList.find{it.img.id == checkedId}

        if(targetItem != null){
            targetItem.visible = false
            liveImageViewList.value = imageViewList
        }
    }



    fun swapImageView(fromPos: Int, toPos: Int){
        Collections.swap(imageViewList,fromPos,toPos)
        liveImageViewList.value = imageViewList
    }




    val selectBackgroundItem : MutableLiveData<Int> = MutableLiveData(-1)
    val selectBackgroundScale : MutableLiveData<FrameLayout.LayoutParams> = MutableLiveData()

    val backGroundColor : MutableLiveData<Int> = MutableLiveData()


   fun selectBackgroundColor(position: Int){
       backGroundColor.value = ColorList.colorsList[position]
   }



    fun selectBackgroundScale(item:Int){


        var layoutParams = FrameLayout.LayoutParams(0,0)
        when(item){
            0->{
                layoutParams  = FrameLayout.LayoutParams(screenWith, screenWith)
            }
            1->{
                val heightPx = screenWith * 0.75
                layoutParams = FrameLayout.LayoutParams(screenWith, heightPx.toInt())
            }
            2->{
                val withPx = screenWith * 0.75
                layoutParams = FrameLayout.LayoutParams(withPx.toInt(),screenWith)
            }
            3->{
                val heightPx = screenWith * 0.66
                layoutParams = FrameLayout.LayoutParams(screenWith, heightPx.toInt())
            }
            4->{
                val withPx = screenWith * 0.66
                layoutParams = FrameLayout.LayoutParams(withPx.toInt(),screenWith)
            }
            5->{
                val heightPx = screenWith * 0.5625
                layoutParams = FrameLayout.LayoutParams(screenWith, heightPx.toInt())
            }
            6->{
                val withPx = screenWith * 0.5625
                layoutParams = FrameLayout.LayoutParams(withPx.toInt(),screenWith)
            }
        }
        selectBackgroundItem.value = item
        selectBackgroundScale.value = layoutParams

    }

    val imgSearch : MutableLiveData<String> = MutableLiveData()

    val unsplashList: MutableLiveData<MutableList<UnsplashData>> = MutableLiveData()
    val selectBackGroundImage : MutableLiveData<String> = MutableLiveData()

    fun clickedImageSearch(){
        viewModelScope.launch {
            val keword = imgSearch.value
            RetrofitApi.getRandomPhotos(keword)?.let {
                unsplashList.value = it.toMutableList()
            }
        }
    }

    fun selectImage(position: Int){
        val list = unsplashList.value
        if(list != null){
            if(list.size >=position){
                selectBackGroundImage.value = list[position].urls.full
            }
        }
    }
}