package com.example.img_decorat.dataModels

import android.graphics.Bitmap
import com.example.img_decorat.ui.view.EditableImageView
import java.util.LinkedList

data class ImgLayerData(
    val bitMap : Bitmap,
    var check : Boolean = false,
    val id : Int,
    var select : Boolean = false
)

data class ImageViewData(
    val img : EditableImageView,
    var visible : Boolean = true,
)

data class ImageSize(
    var with : Int,
    var height : Int
)


data class ListData(
    val layerList : LinkedList<ImgLayerData>,
    val viewList : LinkedList<ImgLayerData>,

)