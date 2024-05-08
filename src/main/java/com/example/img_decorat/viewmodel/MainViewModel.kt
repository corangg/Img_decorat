package com.example.img_decorat.viewmodel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.img_decorat.ImgLayerData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(){
    val selectImg : MutableLiveData<Unit> = MutableLiveData()
    val imgTitle : MutableLiveData<String> = MutableLiveData("New_Image")
    val openGalleryEvent : MutableLiveData<Unit> = MutableLiveData()
    val openMenuEvent : MutableLiveData<Boolean> = MutableLiveData()


    val imagesList = LinkedList<ImgLayerData>()
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

    fun setImgLayerList(data: Intent?){
        data?.clipData?.let{ clipData ->
            for (i in 0 until clipData.itemCount) {
                val imageUri: Uri = clipData.getItemAt(i).uri
                val layerData = ImgLayerData(imageUri,false)

                imagesList.add(layerData)
            }
        } ?: data?.data?.let { uri ->
            val layerData = ImgLayerData(uri,false)
            imagesList.add(layerData)
        }

        layerList.value = imagesList
    }

    fun updateChecked(position: Int, checked: Boolean){
        if(imagesList.size > position){
            val uri = imagesList[position].uri
            val layerData = ImgLayerData(uri,checked)
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

    }

    fun changeLayer(fromPos: Int, toPos: Int){
        if(imagesList.size>fromPos||imagesList.size>toPos){
            val data = imagesList[toPos]

            imagesList.set(toPos,imagesList[fromPos])
            imagesList.set(fromPos,data)

            layerList.value = imagesList

        }

    }

    /*fun updateLayerList(list: LinkedList<Uri>){
        layerList.value = list
    }*/

}