package com.example.img_decorat

import android.graphics.Bitmap
import android.net.Uri

data class ImgLayerData(
    val bitMap : Bitmap,
    val check : Boolean = false,
    val id : Int
)