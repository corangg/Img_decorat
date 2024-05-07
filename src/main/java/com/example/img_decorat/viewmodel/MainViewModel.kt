package com.example.img_decorat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(){
    val selectImg : MutableLiveData<Unit> = MutableLiveData()
    val imgTitle : MutableLiveData<String> = MutableLiveData("New_Image")


}