package com.example.img_decorat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.img_decorat.data.model.dataModels.LoadData
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.data.repository.DBRepository
import com.example.img_decorat.data.repository.SaveDataRepository
import com.example.img_decorat.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveDataViewModel @Inject constructor(
    application: Application,
    private val saveDataRepository: SaveDataRepository,
    private val dbRepository: DBRepository
) : AndroidViewModel(application){
    val dataTitleList : MutableLiveData<List<LoadData>> = MutableLiveData()

    init {
        getViewData()
    }

    fun getViewData(){
        viewModelScope.launch {
            dbRepository.getViewData()?.let {
                dataTitleList.value = saveDataRepository.setLoadTitle(it)
            }
        }
    }


}