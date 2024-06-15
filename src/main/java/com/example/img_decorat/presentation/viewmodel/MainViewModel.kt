package com.example.img_decorat.presentation.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.img_decorat.R
import com.example.img_decorat.data.model.dataModels.EmojiList
import com.example.img_decorat.data.model.dataModels.EmojiUseCases
import com.example.img_decorat.data.model.dataModels.Hue
import com.example.img_decorat.data.model.dataModels.ImageManagementUseCases
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.data.model.dataModels.LayerListUseCases
import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.data.model.dataModels.unsplashimagedata.UnsplashData
import com.example.img_decorat.data.repository.DBRepository
import com.example.img_decorat.data.repository.HueRepositoryImpl
import com.example.img_decorat.data.repository.TextViewRepositoryImpl
import com.example.img_decorat.data.source.remote.retrofit.EmojiRetrofit
import com.example.img_decorat.data.source.remote.retrofit.UnsplashRetrofit
import com.example.img_decorat.domain.usecase.backgroundusecase.SetBackgroundScaleUseCase
import com.example.img_decorat.utils.UtilList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val layerListUseCases: LayerListUseCases,
    private val imageManagementUseCases: ImageManagementUseCases,
    private val setBackgroundScaleUseCase: SetBackgroundScaleUseCase,
    private val emojiUseCases: EmojiUseCases,
    private val dbRepository: DBRepository,
    private val hueRepositoryImpl: HueRepositoryImpl,
    private val textViewRepositoryImpl: TextViewRepositoryImpl
) : AndroidViewModel(application) {
    val startloading: MutableLiveData<Boolean> = MutableLiveData(false)

    val imgTitle: MutableLiveData<String> = MutableLiveData("New_Image")
    val imgSearch: MutableLiveData<String> = MutableLiveData()
    val selectBackGroundImage: MutableLiveData<String> = MutableLiveData()

    val selectNavigationItem: MutableLiveData<Int> = MutableLiveData(0)
    val selectbackgroundMenu: MutableLiveData<Int> = MutableLiveData(0)
    val selectTextMenu: MutableLiveData<Int> = MutableLiveData(0)
    val lastTouchedImageId: MutableLiveData<Int> = MutableLiveData(-1)
    val selectBackgroundItem: MutableLiveData<Int> = MutableLiveData(0)
    val backGroundColor: MutableLiveData<Int> = MutableLiveData()
    val imageSaturationValue: MutableLiveData<Int> = MutableLiveData(0)
    val imageBrightnessValue: MutableLiveData<Int> = MutableLiveData(0)
    val imageTransparencyValue: MutableLiveData<Int> = MutableLiveData(100)
    val emojiTab: MutableLiveData<Int> = MutableLiveData(0)
    val textSize: MutableLiveData<Int> = MutableLiveData(24)
    val overflowMenuToast: MutableLiveData<Int> = MutableLiveData(-1)
    val showToast: MutableLiveData<Int> = MutableLiveData()

    val openGalleryEvent: MutableLiveData<Unit> = MutableLiveData()
    val openSaveDataActivity: MutableLiveData<Unit> = MutableLiveData()
    val openMenuEvent: MutableLiveData<Boolean> = MutableLiveData()
    val openDrawerLayout: MutableLiveData<Unit> = MutableLiveData()

    val liveLayerList: MutableLiveData<LinkedList<LayerItemData>> =
        MutableLiveData(LinkedList<LayerItemData>())
    val liveViewList: MutableLiveData<LinkedList<ViewItemData>> =
        MutableLiveData(LinkedList<ViewItemData>())

    val unsplashList: MutableLiveData<List<UnsplashData>> = MutableLiveData()
    val emojiList: MutableLiveData<List<EmojiList>> = MutableLiveData(listOf())

    var listData: ListData = ListData(LinkedList<LayerItemData>(), LinkedList<ViewItemData>())

    val selectBackgroundScale: MutableLiveData<FrameLayout.LayoutParams> = MutableLiveData()

    lateinit var lastTouchedImage: Uri
    var selectBackgroundScaleType: Float = -1f
    var screenSize: Int = 0


    init {
        getDB()
        imageSaturationValue.observeForever {
            listData.viewList = hueRepositoryImpl.editViewSaturation(listData, it)
        }
        imageBrightnessValue.observeForever {
            listData.viewList = hueRepositoryImpl.editImageViewBrightness(listData, it)
        }
        imageTransparencyValue.observeForever {
            listData.viewList = hueRepositoryImpl.editViewTransparency(listData, it)
        }
    }

    fun getDB() {
        viewModelScope.launch {
            getImojiDB()
        }
    }

    fun setViewSize(size: Int) {
        screenSize = size
        selectBackgroundScale(0)
    }

    fun openDrawerLayout() {
        openDrawerLayout.value = Unit
    }

    fun openGallery() {
        openGalleryEvent.value = Unit
    }

    fun showOverFlowMenu() {
        if (openMenuEvent.value == true) {
            openMenuEvent.value = false
        } else {
            openMenuEvent.value = true
        }
    }

    fun closeOverFlowMenu() {
        openMenuEvent.value = false
    }

    fun bottomNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_navi_background -> {
                selectNavigationItem.value = 0
                return true
            }

            R.id.menu_navi_split -> {
                clickedNavSplit()
                return true
            }

            R.id.menu_navi_hue -> {
                selectNavigationItem.value = 1
                return true
            }

            R.id.menu_navi_sticker -> {
                selectNavigationItem.value = 2
                return true
            }

            R.id.munu_navi_text -> {
                clickedNavText()
                return true
            }

            R.id.menu_background_scale -> {
                selectbackgroundMenu.value = 0
                return true
            }

            R.id.menu_background_paint -> {
                selectbackgroundMenu.value = 1
                return true
            }

            R.id.menu_background_image -> {
                selectbackgroundMenu.value = 2
                return true
            }

            R.id.menu_text_font -> {
                selectTextMenu.value = 0
                return true
            }

            R.id.menu_text_paint -> {
                selectTextMenu.value = 1
                return true
            }

            R.id.menu_text_size -> {
                selectTextMenu.value = 2
                textSize.observeForever {
                    lastTouchedImageId.value?.let { id ->
                        textViewRepositoryImpl.editTextViewSetTextSize(listData.viewList, id, it)
                    }
                }
                return true
            }
        }
        return false
    }

    private fun makeListData(): ListData? {
        return if (liveLayerList.value != null && liveViewList.value != null) {
            ListData(liveLayerList.value!!, liveViewList.value!!)
        } else {
            null
        }
    }

    private fun setList() {
        liveLayerList.value = listData.layerList
        liveViewList.value = listData.viewList
    }

    fun imgAddLayerList(data: Intent?) {
        makeListData()?.let { list ->
            layerListUseCases.imageAddListUseCase.execute(data, list, screenSize)?.let {
                listData = it
                liveLayerList.value = listData.layerList
            }
        }
    }

    fun updateChecked(position: Int, checked: Boolean) {
        if (layerListSizeCheck(position)) {
            layerListUseCases.checkedListUseCase.excute(listData, position, checked)
            setList()
        }
    }

    fun deleteLayer(position: Int) {
        if (layerListSizeCheck(position)) {
            layerListUseCases.deleteListUseCase.excute(listData, position)
            setList()
        }
    }

    fun selectLayer(position: Int) {
        if (layerListSizeCheck(position)) {
            listData.layerList =
                layerListUseCases.selectLayerUseCase.excute(listData.layerList, position)
            liveLayerList.value = listData.layerList
            lastTouchedImageId.value = listData.layerList[position].id
        }
    }

    fun selectLastImage(id: Int) {
        if (layerListUseCases.checkLastSelectImageUseCase.excute(listData.layerList, id)) {
            listData.layerList =
                layerListUseCases.selectLastImageUseCase.excute(listData.layerList, id)
            setList()
            lastTouchedImageId.value = id

            setHue()?.let {
                hueRepositoryImpl.checkHue(listData.viewList, id, it)?.let {
                    imageSaturationValue.value = it.saturatio
                    imageBrightnessValue.value = it.brightness
                    imageTransparencyValue.value = it.transparency
                }
            }
        }
    }

    private fun setHue(): Hue? {
        imageSaturationValue.value?.let { saturatio ->
            imageBrightnessValue.value?.let { brightness ->
                imageTransparencyValue.value?.let { transparency ->
                    return Hue(saturatio, brightness, transparency)
                }
            }
        }
        return null
    }

    fun swapImageView(fromPos: Int, toPos: Int) {
        liveViewList.value =
            layerListUseCases.swapImageViewUseCase.excute(listData.viewList, fromPos, toPos)
    }

    fun selectBackgroundColor(color: Int) {
        backGroundColor.value = color
    }

    fun selectBackgroundScale(item: Int) {
        selectBackgroundItem.value = item
        selectBackgroundScale.value = setBackgroundScaleUseCase.execute(item, screenSize)
        selectBackgroundScaleType = item.toFloat()
    }

    fun clickedImageSearch() {
        viewModelScope.launch {
            startloading.value = true
            UnsplashRetrofit.getRandomPhotos(imgSearch.value)?.let {
                unsplashList.value = it
            }
            startloading.value = false
        }
    }

    fun selectImage(position: Int) {
        val list = unsplashList.value
        list?.let {
            if (list.size >= position) {
                selectBackGroundImage.value = list[position].urls.full
            }
        }
    }

    fun reseultSplitView(uri: Uri) {
        listData = layerListUseCases.splitImageChangeListUseCase.excute(listData, uri)
        setList()
    }

    fun getEmoji() {
        viewModelScope.launch {
            EmojiRetrofit.getEmojis()?.let {
                val list = emojiUseCases.emojiDBListClassificationUseCase.excute(it)
                emojiList.value = emojiUseCases.emojiDataStringToBitmapUseCase.excute(list)
                dbRepository.insertEmojiData(list)
            }
        }
    }

    suspend fun getImojiDB() {
        val getData = dbRepository.getEmojiData()
        getData?.let {
            if (it.size == 0) {
                getEmoji()
            } else {
                emojiList.value = emojiUseCases.emojiDataStringToBitmapUseCase.excute(it)
            }
        }
    }

    fun addEmogeLayer(emojiPosition: Int) {
        emojiList.value?.let { list ->
            emojiTab.value?.let { tab ->
                listData = emojiUseCases.emojiAddLayerUseCase.excute(
                    listData,
                    screenSize,
                    list[tab].groupList[emojiPosition]
                )
                setList()
            }
        }
    }

    fun textColorSet(position: Int) {//근데 이러면 뷰에 색은 안바뀔거 같은데??
        lastTouchedImageId.value?.let { id ->
            textViewRepositoryImpl.editTextViewSetTextColor(
                listData,
                id,
                UtilList.colorsList[position]
            )?.let {
                listData = it
                liveLayerList.value = listData.layerList
            }
        }
    }

    fun textBackgroundColorSet(position: Int) {
        lastTouchedImageId.value?.let { id ->
            textViewRepositoryImpl.editTextViewSetBackgroundColor(
                listData,
                id,
                UtilList.colorsList[position]
            )?.let {
                listData = it
                liveLayerList.value = listData.layerList
            }
        }
    }


    fun textFontSet(position: Int) {
        val font = UtilList.typefaces[position]
        lastTouchedImageId.value?.let { id ->
            textViewRepositoryImpl.editTextViewSetFont(listData, id, font)?.let {
                listData = it
                liveLayerList.value = listData.layerList
            }
        }
    }

    fun setViewText(text: String) {
        lastTouchedImageId.value?.let {
            listData.layerList = textViewRepositoryImpl.layerViewUpdateText(listData, it, text)
            liveLayerList.value = listData.layerList
        }
    }

    fun selectToolbarMenu(position: Int, view: FrameLayout) {
        closeOverFlowMenu()
        when (position) {
            0 -> {
                openSaveDataActivity.value = Unit
            }

            1 -> {
                startloading.value = true
                saveViewData(view)
                overflowMenuToast.value = position
            }

            2 -> {
                startloading.value = true
                val flagLastTouchedImageId = lastTouchedImageId.value
                lastTouchedImageId.value = -1
                imageManagementUseCases.editViewUseCase.execute(view, imgTitle.value!!)
                overflowMenuToast.value = position
                lastTouchedImageId.value = flagLastTouchedImageId!!
                startloading.value = false
                showToast.value = 2
            }
        }

    }

    fun loadData(name: String?) {
        name?.let {
            imgTitle.value = it
            getSaveData(name)
        }
    }

    private fun getSaveData(key: String) {
        viewModelScope.launch {
            val data = dbRepository.getViewData(key)
            data?.let {
                FrameLayoutSet(data)
                listData = layerListUseCases.loadListUseCase.excute(it, screenSize)
                setList()
            }
        }
    }

    private fun FrameLayoutSet(data: SaveViewData) {
        val scaleType = data.scale.toInt()
        selectBackgroundScale(scaleType)
        selectBackgroundColor(data.bgColor)
        if (data.bgImg != "") {
            selectBackGroundImage.value = data.bgImg
        }
    }

    private fun saveViewData(view: FrameLayout) {
        viewModelScope.launch {
            val flagLastTouchedImageId = lastTouchedImageId.value
            lastTouchedImageId.value = -1
            val list = liveViewList.value
            val name = imgTitle.value
            if (list != null && name != null) {
                val data = imageManagementUseCases.saveViewUseCase.execute(
                    list = list,
                    view = view,
                    scale = selectBackgroundScaleType,
                    name = name
                )
                dbRepository.insertViewData(data)
            }
            lastTouchedImageId.value = flagLastTouchedImageId!!
            startloading.value = false
            showToast.value = 1
        }
    }

    private fun clickedNavSplit() {
        val lastTouchedId = lastTouchedImageId.value
        lastTouchedId?.let { id ->
            if (layerListUseCases.checkedViewTypeUseCase.excute(listData.layerList, id) == 0) {
                val uri =
                    layerListUseCases.setLastTouchedImageUseCase.excute(listData.layerList, id)
                uri?.let {
                    lastTouchedImage = it
                    selectNavigationItem.value = 4
                }
            } else {
                showToast.value = 0
            }
        }
    }

    private fun clickedNavText() {
        lastTouchedImageId.value?.let {
            if (textViewRepositoryImpl.checkEditableTextView(listData.viewList, it)) {
                listData = textViewRepositoryImpl.editTextViewAddList(listData, screenSize)
                setList()
                selectLayer(listData.layerList.size - 1)
            }
            selectNavigationItem.value = 3
        }
    }

    private fun layerListSizeCheck(position: Int): Boolean {
        liveLayerList.value?.let {
            if (it.size > position) {
                return true
            }
        }
        return false
    }
}