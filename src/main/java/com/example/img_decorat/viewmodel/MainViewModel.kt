package com.example.img_decorat.viewmodel

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.R
import com.example.img_decorat.data.model.dataModels.EmojiData
import com.example.img_decorat.data.model.dataModels.EmojiList
import com.example.img_decorat.data.source.remote.retrofit.UnsplashRetrofit
import com.example.img_decorat.data.model.dataModels.unsplashimagedata.UnsplashData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.data.repository.BackgroundRepository
import com.example.img_decorat.data.repository.ImageManagementRepository
import com.example.img_decorat.data.source.remote.retrofit.EmojiRetrofit
import com.example.img_decorat.data.repository.LayerListRepository
import com.example.img_decorat.utils.APIKey
import com.example.img_decorat.utils.UtilList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val layerListRepository: LayerListRepository,
    private val backgroundRepository: BackgroundRepository,
    private val imageManagementRepository: ImageManagementRepository
) : AndroidViewModel(application){
    var screenSize : Int = 0

    val imgTitle : MutableLiveData<String> = MutableLiveData("New_Image")
    val imgSearch : MutableLiveData<String> = MutableLiveData()
    val selectBackGroundImage : MutableLiveData<String> = MutableLiveData()

    val selectNavigationItem : MutableLiveData<Int> = MutableLiveData(0)
    val selectbackgroundMenu: MutableLiveData<Int> = MutableLiveData(0)
    val selectTextMenu : MutableLiveData<Int> = MutableLiveData(0)
    val lastTouchedImageId : MutableLiveData<Int> = MutableLiveData(-1)
    val selectBackgroundItem : MutableLiveData<Int> = MutableLiveData(0)
    val backGroundColor : MutableLiveData<Int> = MutableLiveData()

    val openGalleryEvent : MutableLiveData<Unit> = MutableLiveData()
    val openMenuEvent : MutableLiveData<Boolean> = MutableLiveData()

    val liveLayerList : MutableLiveData<LinkedList<LayerItemData>> = MutableLiveData(LinkedList<LayerItemData>())
    val liveViewList : MutableLiveData<LinkedList<ViewItemData>> = MutableLiveData(LinkedList<ViewItemData>())

    val unsplashList: MutableLiveData<List<UnsplashData>> = MutableLiveData()
    val emojiList: MutableLiveData<List<EmojiList>> = MutableLiveData(listOf())

    val imageSaturationValue : MutableLiveData<Int> = MutableLiveData(0)
    val imageBrightnessValue : MutableLiveData<Int> = MutableLiveData(0)
    val imageTransparencyValue : MutableLiveData<Int> = MutableLiveData(100)
    val emojiTab : MutableLiveData<Int> = MutableLiveData(0)
    val textSize : MutableLiveData<Int> = MutableLiveData(24)

    val selectBackgroundScale : MutableLiveData<FrameLayout.LayoutParams> = MutableLiveData()

    lateinit var lastTouchedImage : Uri

    init {
        getEmoji()
        imageSaturationValue.observeForever {
            layerListRepository.editViewSaturation(it)
        }
        imageBrightnessValue.observeForever {
            layerListRepository.editImageViewBrightness(it)
        }
        imageTransparencyValue.observeForever {
            layerListRepository.viewTransparency(it)
        }
    }

    fun setViewSize(size : Int){
        screenSize = size
        selectBackgroundScale(0)
    }

    fun openGallery(){
        openGalleryEvent.value = Unit
    }

    fun showOverFlowMenu(){
        if(openMenuEvent.value == true){
            openMenuEvent.value = false
        }else{
            openMenuEvent.value = true
        }
    }

    fun closeOverFlowMenu(){
        openMenuEvent.value = false
    }

    fun bottomNavigationItemSelected(item : MenuItem):Boolean{
        when(item.itemId){
            R.id.menu_navi_background->{
                selectNavigationItem.value = 0
                return true
            }
            R.id.menu_navi_split->{
                clickedNavSplit()
                return true
            }
            R.id.menu_navi_hue->{
                selectNavigationItem.value = 2
                return true
            }
            R.id.menu_navi_sticker->{
                selectNavigationItem.value = 3
                return true
            }
            R.id.munu_navi_text->{
                clickedNavText()
                return true
            }
            R.id.menu_background_scale->{
                selectbackgroundMenu.value = 0
                return true
            }
            R.id.menu_background_paint->{
                selectbackgroundMenu.value = 1
                return true
            }
            R.id.menu_background_image->{
                selectbackgroundMenu.value = 2
                return true
            }
            R.id.menu_text_font->{
                selectTextMenu.value = 0
                return true
            }
            R.id.menu_text_paint->{
                selectTextMenu.value = 1
                return true
            }
            R.id.menu_text_size->{
                selectTextMenu.value = 2
                textSize.observeForever{
                    layerListRepository.EditTextViewSetTextSize(it)
                }
                return true
            }
        }
        return false
    }

    fun imgAddLayerList(data: Intent?){
        liveLayerList.value = layerListRepository.imgAddList(data,screenSize)
    }

    fun updateChecked(position: Int, checked: Boolean){
        if (layerListSizeCheck(position)){
            liveLayerList.value = layerListRepository.checkedUpdateLayerList(position,checked)
            liveViewList.value = layerListRepository.checkedUpdateViewList(position,checked)
        }
    }

    fun deleteLayer(position: Int){
        if (layerListSizeCheck(position)){
            liveLayerList.value = layerListRepository.deleteLayerList(position)
            liveViewList.value = layerListRepository.deleteImageViewList(position)
        }
    }

    fun selectLayer(position: Int){
        if (layerListSizeCheck(position)){
            val layerList = layerListRepository.selectLayer(position)
            liveLayerList.value = layerList
            lastTouchedImageId.value = layerList[position].id
        }
    }

    fun selectLastImage(id: Int){
        if(layerListRepository.checkLastSelectImage(id)){
            liveLayerList.value = layerListRepository.selectLastImage(id)
            liveViewList.value =layerListRepository.viewList
            lastTouchedImageId.value = id
            layerListRepository.checkSaturatio(imageSaturationValue.value)?.let {
                imageSaturationValue.value = it
            }
            layerListRepository.checkBrightness(imageBrightnessValue.value)?.let {
                imageBrightnessValue.value = it
            }
            layerListRepository.checkTransparency(imageTransparencyValue.value)?.let {
                imageTransparencyValue.value = it
            }
        }
    }

    fun swapImageView(fromPos: Int, toPos: Int){
        liveViewList.value = layerListRepository.swapImageView(fromPos,toPos)
    }

    fun selectBackgroundColor(position: Int){
        backGroundColor.value = UtilList.colorsList[position]
    }

    fun selectBackgroundScale(item:Int){
        selectBackgroundItem.value = item
        selectBackgroundScale.value = backgroundRepository.setBackgroundScale(item, screenSize)

    }

    fun clickedImageSearch(){
        viewModelScope.launch {
            val keword = imgSearch.value
            UnsplashRetrofit.getRandomPhotos(keword)?.let {
                unsplashList.value = it
            }
        }
    }

    fun selectImage(position: Int){
        val list = unsplashList.value
        list?.let {
            if(list.size >=position){
                selectBackGroundImage.value = list[position].urls.full
            }
        }
    }

    fun reseultSplitView(uri:Uri){
        liveLayerList.value = layerListRepository.splitImageChangeList(uri)
        liveViewList.value = layerListRepository.viewList
    }

    fun getEmoji(){
        viewModelScope.launch {
            EmojiRetrofit.getEmojis()?.let {
                emojiList.value = layerListRepository.emojiListClassification(it)
            }
        }
    }

    fun addEmogeLayer(emojiPosition:Int){
        if(emojiList.value != null && emojiTab.value != null){
            liveLayerList.value = layerListRepository.emojiAddLayer(emojiList.value!![emojiTab.value!!].groupList[emojiPosition],screenSize)
            liveViewList.value = layerListRepository.viewList
        }
    }

    fun textColorSet(position: Int){
        liveLayerList.value = layerListRepository.editTextViewSetTextColor(UtilList.colorsList[position])
    }

    fun textBackgroundColorSet(position: Int){
        liveLayerList.value = layerListRepository.editTextViewSetBackgroundColor(UtilList.colorsList[position])
    }

    fun textFontSet(position: Int){
        val font = UtilList.typefaces[position]
        liveLayerList.value = layerListRepository.EditTextViewSetFont(font)
    }

    fun setViewText(text: String){
        liveLayerList.value = layerListRepository.layerViewUpdateText(text)
    }

    fun selectToolbarMenu(position: Int,view: View){
        when(position){
            1->{
                imageManagementRepository.editViewSave(view,imgTitle.value!!)
            }

        }

    }

    private fun clickedNavSplit(){
        val lastTouchedId = lastTouchedImageId.value
        lastTouchedId?.let {id->
            if(layerListRepository.checkedViewType(id) == 0){
                val uri = layerListRepository.setLastTouchedImage(id)
                uri?.let {
                    lastTouchedImage = it
                    selectNavigationItem.value = 1
                }
            }
        }
    }

    private fun clickedNavText(){
        liveLayerList.value = layerListRepository.editTextViewAddList(screenSize)
        liveViewList.value = layerListRepository.viewList
        selectLayer(liveLayerList.value!!.size - 1)
        selectNavigationItem.value = 4
    }

    private fun layerListSizeCheck(position: Int):Boolean{
        liveLayerList.value?.let {
            if(it.size > position){
                return true
            }
        }
        return false
    }
}