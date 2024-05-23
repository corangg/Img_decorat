package com.example.img_decorat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.img_decorat.data.repository.DBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveDataViewModel @Inject constructor(
    application: Application,
    private val dbRepository: DBRepository) : AndroidViewModel(application){

        init {
            getViewData()
        }

        fun getViewData(){
            viewModelScope.launch {
                true
                dbRepository.getViewData()?.let {
                    //getSaveDataList.value = it
                    true
                }?:run {
                    true
                }
            }
        }


}