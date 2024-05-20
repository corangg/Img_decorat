package com.example.img_decorat.dataModels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import com.example.img_decorat.ui.view.EditableImageView
import com.example.img_decorat.ui.view.TextImageView
import java.util.LinkedList

data class ImgLayerData(
    var bitMap : Bitmap
    = Bitmap.createBitmap(1024, 1024, Bitmap.Config.ARGB_8888).apply { (Color.TRANSPARENT) },
    val context: Context,
    var check : Boolean = false,
    val id : Int,
    var select : Boolean = false,
    var type: Int = 0
){
    var text: TextView = TextView(context)
}
/*data class ImageViewData(
    var img : EditableImageView,
    var visible : Boolean = true,
    var saturation : Int = 0,
    var brightness : Int = 0,
    var transparency : Int = 100
)*/

data class ViewListData(
    val context: Context,
    val id : Int,
    var visible : Boolean = true,
    var type : Int = 0,
    var saturation : Int = 0,
    var brightness : Int = 0,
    var transparency : Int = 100
){
    var img: EditableImageView = EditableImageView(context)
    var text: TextImageView = TextImageView(context)
}

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