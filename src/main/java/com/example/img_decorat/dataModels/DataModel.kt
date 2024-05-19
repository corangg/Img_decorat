package com.example.img_decorat.dataModels

import android.graphics.Bitmap
import com.example.img_decorat.ui.view.EditableImageView
import java.util.LinkedList

data class ImgLayerData(
    var bitMap : Bitmap,
    var check : Boolean = false,
    val id : Int,
    var select : Boolean = false
)

data class ImageViewData(
    var img : EditableImageView,
    var visible : Boolean = true,
    var saturation : Int = 0,
    var brightness : Int = 0,
    var transparency : Int = 100
)

data class EmojiList(
    var groupName : String,
    var groupList : MutableList<Bitmap>
)

data class ImageSize(
    var with : Int,
    var height : Int
)


data class ListData(
    val layerList : LinkedList<ImgLayerData>,
    val viewList : LinkedList<ImgLayerData>,

)