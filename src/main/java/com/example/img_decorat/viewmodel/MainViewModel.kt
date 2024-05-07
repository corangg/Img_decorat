package com.example.img_decorat.viewmodel

import android.app.Activity
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(){
    val selectImg : MutableLiveData<Unit> = MutableLiveData()
    val imgTitle : MutableLiveData<String> = MutableLiveData("New_Image")
    val openGalleryEvent : MutableLiveData<Unit> = MutableLiveData()
    val openMenuEvent : MutableLiveData<Boolean> = MutableLiveData()

    val layerList : MutableLiveData<MutableList<Uri>> = MutableLiveData(mutableListOf())//바로 적용안되면 라이브데이터로 바꿔야함


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

    fun updateLayerList(list: MutableList<Uri>){
        layerList.value = list
        true

    }

}