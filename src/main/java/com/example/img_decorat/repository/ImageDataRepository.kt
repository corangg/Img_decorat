package com.example.img_decorat.repository

import android.os.Build
import android.view.View
import androidx.core.view.ViewCompat
import javax.inject.Inject

class ImageDataRepository@Inject constructor() {
    fun setID() : Int{
        val id =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                View.generateViewId()
            } else {
                ViewCompat.generateViewId()
            }
        return id
    }
}