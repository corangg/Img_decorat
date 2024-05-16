package com.example.img_decorat.viewmodel

import android.app.Application
import android.os.Build.VERSION_CODES.M
import android.view.MenuItem
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.img_decorat.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplitViewModel@Inject constructor(application: Application)  : AndroidViewModel(application){

    val selectToolbar : MutableLiveData<Int> = MutableLiveData(-1)
    val selectSplitItem : MutableLiveData<Int> = MutableLiveData(0)

    fun selectToolbarItem(item : Int):Boolean{
        when(item){
            0->{
                selectToolbar.value = 0
                return true
            }
            1->{
                selectToolbar.value = 1
                return true
            }
            2->{
                selectToolbar.value = 2
                    return true
            }
            3->{
                selectToolbar.value = 3
                    return true
            }
        }
        return false
    }

    fun selectSplitItem(item: MenuItem):Boolean{
        when(item.itemId){
            R.id.split_nav_square->{
                return true
            }
            R.id.split_nav_polygon->{
                return true
            }
            R.id.split_nav_freestyle->{
                return true
            }
        }
        return false
    }


}