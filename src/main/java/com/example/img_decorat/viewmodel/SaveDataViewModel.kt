package com.example.img_decorat.viewmodel

import android.app.Application
import android.view.MenuItem
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.img_decorat.R
import com.example.img_decorat.data.model.dataModels.LoadData
import com.example.img_decorat.data.repository.DBRepository
import com.example.img_decorat.data.repository.SaveDataRepository
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
    val openLoadData : MutableLiveData<Boolean> = MutableLiveData()
    val showToastMessage : MutableLiveData<Int> = MutableLiveData(-1)
    var selectItemName : String = ""


    init {
        getViewData()
    }

    fun getViewData(){
        viewModelScope.launch {
            dbRepository.getAllViewData()?.let {
                dataTitleList.value = saveDataRepository.setLoadTitle(it)
            }
        }
    }

    fun clickedLoadItem(type: Int, position: Int){
        val list = dataTitleList.value
        list?.let {
            val deleteItem = it[position].title
            when(type){
                0->{
                    selectItemName = deleteItem
                }
                1->{
                    viewModelScope.launch {
                        dbRepository.deleteViewData(deleteItem)
                        getViewData()
                    }
                }
                else->{}
            }
        }
    }

    fun onMenuSaveDataOpenClicked(item : MenuItem) {
        when(item.itemId){
            R.id.menu_save_data_open->{
                clickedOpenLoadData()
            }
        }
    }

    private fun clickedOpenLoadData(){
        if (selectItemName != ""){
            openLoadData.value = true
        }else{
            showToastMessage.value = 0
        }
    }


}