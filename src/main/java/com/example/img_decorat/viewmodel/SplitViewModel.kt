package com.example.img_decorat.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.view.MenuItem
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.img_decorat.R
import com.example.img_decorat.repository.LayerListRepository
import com.example.img_decorat.repository.SplitRepository
import com.example.img_decorat.ui.view.SplitCircleView
import com.example.img_decorat.ui.view.SplitPolygonView
import com.example.img_decorat.ui.view.SplitSquareVIew
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplitViewModel@Inject constructor(
    application: Application,
    private val splitRepository: SplitRepository,
    private val layerListRepository: LayerListRepository)  : AndroidViewModel(application){

    val selectToolbar : MutableLiveData<Int> = MutableLiveData(-1)
    val selectSplitItem : MutableLiveData<Int> = MutableLiveData(0)
    val splitImage : MutableLiveData<Bitmap> = MutableLiveData()
    val undoStackBoolean : MutableLiveData<Boolean> = MutableLiveData(false)
    val runStackBoolean : MutableLiveData<Boolean> = MutableLiveData(false)
    val polygonPoint : MutableLiveData<Int> = MutableLiveData(3)

    val splitSquareView : MutableLiveData<SplitSquareVIew> = MutableLiveData()
    val splitCircleView : MutableLiveData<SplitCircleView> = MutableLiveData()
    val splitPolygonView : MutableLiveData<SplitPolygonView> = MutableLiveData()


    init {
        splitSquareView.value = splitRepository.squareSplitView()
        splitCircleView.value = splitRepository.circleSplitView()
        splitPolygonView.value = splitRepository.polygoneSplitView()
        splitRepository.resetUndoStack()
        splitRepository.resetRunStack()
    }



    fun selectToolbarItem(item : Int):Boolean{
        when(item){
            0->{
                selectToolbar.value = 0
                return true
            }
            1->{
                selectToolbar.value = 1
                if(splitRepository.checkUndo()){
                    splitRepository.addRunStack(splitImage.value)
                    splitImage.value = splitRepository.popUndo()
                    undoStackBoolean.value = splitRepository.checkUndo()
                    runStackBoolean.value = splitRepository.checkRun()
                }
                return true
            }
            2->{
                selectToolbar.value = 2
                if(splitRepository.checkRun()){
                    splitRepository.addUndoStack(splitImage.value)
                    splitImage.value = splitRepository.popRun()
                    undoStackBoolean.value = splitRepository.checkUndo()
                    runStackBoolean.value = splitRepository.checkRun()
                }
                    return true
            }
            3->{
                selectToolbar.value = 3
                splitRepository.addUndoStack(splitImage.value)
                splitItem()
                splitRepository.resetRunStack()
                undoStackBoolean.value = splitRepository.checkUndo()
                runStackBoolean.value = splitRepository.checkRun()
                    return true
            }
            4->{
                selectToolbar.value = 4
                return true
            }
        }
        return false
    }

    fun selectSplitItem(item: MenuItem):Boolean{
        when(item.itemId){
            R.id.split_nav_square->{
                selectSplitItem.value = 0
                return true
            }
            R.id.split_nav_circle->{
                selectSplitItem.value = 1
                return true
            }
            R.id.split_nav_polygon->{
                selectSplitItem.value = 2
                return true
            }
            R.id.split_nav_freestyle->{
                return true
            }
        }
        return false
    }

    fun intentToBitmap(uri : String){
        splitImage.value = layerListRepository.uriToBitmap(uri.toUri())
    }

    fun splitItem(){
        when(selectSplitItem.value){
            0->{
                splitImage.value = splitRepository.cropSquareImage(splitSquareView.value!!,splitImage.value!!)
            }
            1->{
                splitImage.value = splitRepository.cropCircleImage(splitCircleView.value!!,splitImage.value!!)
            }
            2->{
                splitImage.value = splitRepository.cropPolygonImage(splitPolygonView.value!!,splitImage.value!!)
            }
        }
    }
}