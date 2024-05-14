package com.example.img_decorat.dataModels

import android.graphics.Bitmap
import com.example.img_decorat.ZoomableImageView

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


