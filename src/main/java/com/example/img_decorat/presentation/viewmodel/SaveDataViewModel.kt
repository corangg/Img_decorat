package com.example.img_decorat.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.img_decorat.data.model.dataModels.LoadData
import com.example.img_decorat.data.repository.DBRepository
import com.example.img_decorat.domain.usecase.saveusecase.SaveSetLoadTitleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveDataViewModel @Inject constructor(
    application: Application,
    private val saveSetLoadTitleUseCase: SaveSetLoadTitleUseCase,
    private val dbRepository: DBRepository
) : AndroidViewModel(application) {
    val dataTitleList: MutableLiveData<List<LoadData>> = MutableLiveData()
    val openLoadData: MutableLiveData<Boolean> = MutableLiveData()
    val showToastMessage: MutableLiveData<Int> = MutableLiveData(-1)
    val finisheLoadDataActivity: MutableLiveData<Unit> = MutableLiveData()
    val recyclerLayoutManagerSet: MutableLiveData<Boolean> = MutableLiveData()
    var selectItemName: String = ""

    init {
        getViewData()
    }

    fun getViewData() {
        viewModelScope.launch {
            dataTitleList.value = saveSetLoadTitleUseCase.excute(dbRepository.getAllViewData())
            clickedLinearRecycler()
        }
    }

    fun clickedLoadItem(type: Int, position: Int) {
        val list = dataTitleList.value
        list?.let {
            val deleteItem = it[position].title
            when (type) {
                0 -> {
                    selectItemName = deleteItem
                }

                1 -> {
                    viewModelScope.launch {
                        dbRepository.deleteViewData(deleteItem)
                        getViewData()
                    }
                }

                else -> {}
            }
        }
    }

    fun clickedBack() {
        finisheLoadDataActivity.value = Unit
    }

    fun clickedOpenLoadData() {
        if (selectItemName != "") {
            openLoadData.value = true
        } else {
            showToastMessage.value = 0
        }
    }

    fun clickedLinearRecycler() {
        recyclerLayoutManagerSet.value = true
    }

    fun clickedGrideRecycler() {
        recyclerLayoutManagerSet.value = false
    }
}