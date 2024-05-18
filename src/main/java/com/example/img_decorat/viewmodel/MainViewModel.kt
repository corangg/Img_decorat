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
import com.example.img_decorat.repository.BackgroundRepository
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
    private val layerListRepository: LayerListRepository,
    private val backgroundRepository: BackgroundRepository) : AndroidViewModel(application){
    var screenWith : Int = 0

    val imgTitle : MutableLiveData<String> = MutableLiveData("New_Image")
    val imgSearch : MutableLiveData<String> = MutableLiveData()
    val selectBackGroundImage : MutableLiveData<String> = MutableLiveData()

    val selectNavigationItem : MutableLiveData<Int> = MutableLiveData(0)
    val selectbackgroundMenu: MutableLiveData<Int> = MutableLiveData(0)
    val lastTouchedImageId : MutableLiveData<Int> = MutableLiveData(-1)
    val selectBackgroundItem : MutableLiveData<Int> = MutableLiveData(-1)
    val backGroundColor : MutableLiveData<Int> = MutableLiveData()

    val openGalleryEvent : MutableLiveData<Unit> = MutableLiveData()
    val openMenuEvent : MutableLiveData<Boolean> = MutableLiveData()

    val liveLayerList : MutableLiveData<LinkedList<ImgLayerData>> = MutableLiveData(LinkedList<ImgLayerData>())
    val liveImageViewList : MutableLiveData<LinkedList<ImageViewData>> = MutableLiveData(LinkedList<ImageViewData>())
    val selectBackgroundScale : MutableLiveData<FrameLayout.LayoutParams> = MutableLiveData()
    val unsplashList: MutableLiveData<MutableList<UnsplashData>> = MutableLiveData()

    lateinit var lastTouchedImage : Uri



    fun bottomNavigationItemSelected(item : MenuItem):Boolean{
        when(item.itemId){
            R.id.navigation_background->{
                selectNavigationItem.value = 0
                return true
            }
            R.id.navigation_split->{
                val lastTouchedId = lastTouchedImageId.value
                if(lastTouchedId != -1 && lastTouchedId != null){
                    lastTouchedImage = layerListRepository.setLastTouchedImage(lastTouchedId)
                    selectNavigationItem.value = 1
                }
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

    fun setImgLayerList(data: Intent?){
        liveLayerList.value = layerListRepository.setImgLayerList(data)
    }

    fun updateChecked(position: Int, checked: Boolean){
        if(liveLayerList.value!!.size > position){
            liveLayerList.value = layerListRepository.updateLayerListChecked(position,checked)
        }
        liveImageViewList.value = layerListRepository.updateImageViewListChecked(position,checked)
    }


    fun deleteLayer(position: Int){
        if(liveLayerList.value!!.size > position){
            liveLayerList.value = layerListRepository.deleteLayerList(position)
        }
        liveImageViewList.value = layerListRepository.deleteImageViewList(position)
    }

    fun addLayer(){
        liveLayerList.value = layerListRepository.addLayer()
    }

    fun selectLayer(position: Int){
        if(liveLayerList.value!!.size > position){
            val layerList = layerListRepository.selectLayer(position)
            liveLayerList.value = layerList
            lastTouchedImageId.value = layerList[position].id
        }
    }

    fun selectLastImage(id: Int){
        liveLayerList.value = layerListRepository.selectLastImage(id)
        lastTouchedImageId.value = id
    }

    fun swapImageView(fromPos: Int, toPos: Int){
        liveImageViewList.value = layerListRepository.swapImageView(fromPos,toPos)
    }

    fun selectBackgroundColor(position: Int){
        backGroundColor.value = ColorList.colorsList[position]
    }

    fun selectBackgroundScale(item:Int){
        selectBackgroundItem.value = item
        selectBackgroundScale.value = backgroundRepository.setBackgroundScale(item, screenWith)

    }

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

    fun reseultSplitView(uri:Uri){
        liveLayerList.value = layerListRepository.addSplitImage(uri)
    }
}