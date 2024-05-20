package com.example.img_decorat

import android.app.Application
import com.example.img_decorat.utils.FontsList
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class I_Kku: Application() {
    override fun onCreate() {
        super.onCreate()
        FontsList.initialize(this)
    }
}