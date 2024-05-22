package com.example.img_decorat

import android.app.Application
import com.example.img_decorat.utils.UtilList
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class I_Kku: Application() {
    override fun onCreate() {
        super.onCreate()
        UtilList.initialize(this)
    }
}