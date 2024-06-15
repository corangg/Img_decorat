package com.example.img_decorat.presentation.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.view.MenuItem
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.img_decorat.R
import com.example.img_decorat.data.repository.SplitRepository
import com.example.img_decorat.data.repository.SplitStackRepository
import com.example.img_decorat.presentation.ui.view.SplitCircleView
import com.example.img_decorat.presentation.ui.view.SplitPolygonView
import com.example.img_decorat.presentation.ui.view.SplitSquareView
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplitViewModel @Inject constructor(
    application: Application,
    private val splitStackRepository: SplitStackRepository,
    private val splitRepository: SplitRepository
) : AndroidViewModel(application) {
    val previousStackState: MutableLiveData<Boolean> = MutableLiveData(false)
    val nextStackState: MutableLiveData<Boolean> = MutableLiveData(false)

    val selectToolbar: MutableLiveData<Int> = MutableLiveData()
    val selectSplitItem: MutableLiveData<Int> = MutableLiveData(0)
    val polygonPoint: MutableLiveData<Int> = MutableLiveData(3)

    val currentImage: MutableLiveData<Bitmap> = MutableLiveData()

    val splitSquareView: MutableLiveData<SplitSquareView> = MutableLiveData()
    val splitCircleView: MutableLiveData<SplitCircleView> = MutableLiveData()
    val splitPolygonView: MutableLiveData<SplitPolygonView> = MutableLiveData()

    lateinit var splitImageUri: Uri

    init {
        clearStack()
        createSplitView()
    }

    private fun clearStack() {
        splitStackRepository.clearPreviousStack()
        splitStackRepository.clearNextStack()
    }

    private fun createSplitView() {
        splitSquareView.value = splitRepository.squareSplitView()
        splitCircleView.value = splitRepository.circleSplitView()
        splitPolygonView.value = splitRepository.polygoneSplitView()
    }

    fun intentToBitmap(uri: String) {
        splitRepository.getIntentBitmap(uri.toUri())?.let {
            currentImage.value = it
        }
    }

    fun selectToolbarItem(item: Int): Boolean {
        when (item) {
            0 -> {
                clickedBackButoon(item)
                return true
            }

            1 -> {
                clickedPreviousButoon(item)
                return true
            }

            2 -> {
                clickedNextButoon(item)
                return true
            }

            3 -> {
                clickedSplitButoon(item)
                return true
            }

            4 -> {
                clickedCompleteButoon(item)
                return true
            }
        }
        return false
    }

    fun selectSplitItem(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_split_square -> {
                selectSplitItem.value = 0
                return true
            }

            R.id.menu_split_circle -> {
                selectSplitItem.value = 1
                return true
            }

            R.id.menu_split_polygon -> {
                selectSplitItem.value = 2
                return true
            }
        }
        return false
    }

    private fun checkStack() {
        previousStackState.value = splitStackRepository.checkPreviousStackState()
        nextStackState.value = splitStackRepository.checkNextStackState()
    }

    private fun clickedBackButoon(item: Int) {
        selectToolbar.value = item
    }

    private fun clickedPreviousButoon(item: Int) {
        selectToolbar.value = item
        if (splitStackRepository.checkPreviousStackState()) {
            splitStackRepository.addNextStack(currentImage.value)
            currentImage.value = splitStackRepository.popPrevious()
            checkStack()
        }
    }

    private fun clickedNextButoon(item: Int) {
        selectToolbar.value = item
        if (splitStackRepository.checkNextStackState()) {
            splitStackRepository.addPreviousStack(currentImage.value)
            currentImage.value = splitStackRepository.popNext()
            checkStack()
        }
    }

    private fun clickedSplitButoon(item: Int) {
        selectToolbar.value = item
        splitStackRepository.addPreviousStack(currentImage.value)
        splitImage()
        splitStackRepository.clearNextStack()
        checkStack()
    }

    private fun clickedCompleteButoon(item: Int) {
        val image = currentImage.value
        image?.let {
            splitRepository.setIntentUri(it)?.let {
                splitImageUri = it
                selectToolbar.value = item
            }
        }
    }

    private fun splitImage() {
        when (selectSplitItem.value) {
            0 -> {
                currentImage.value =
                    splitRepository.cropSquareImage(splitSquareView.value!!, currentImage.value!!)
            }

            1 -> {
                currentImage.value =
                    splitRepository.cropCircleImage(splitCircleView.value!!, currentImage.value!!)
            }

            2 -> {
                currentImage.value =
                    splitRepository.cropPolygonImage(splitPolygonView.value!!, currentImage.value!!)
            }
        }
    }
}