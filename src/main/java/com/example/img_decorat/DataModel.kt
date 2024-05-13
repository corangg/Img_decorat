package com.example.img_decorat

import android.graphics.Bitmap
import android.net.Uri

data class ImgLayerData(
    val bitMap : Bitmap,
    var check : Boolean = false,
    val id : Int,
    var select : Boolean = false
)

data class ImageViewData(
    val img : ZoomableImageView,
    var visible : Boolean = true,
)