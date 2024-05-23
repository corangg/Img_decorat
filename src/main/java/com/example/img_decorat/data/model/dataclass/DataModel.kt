package com.example.img_decorat.data.model.dataModels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.widget.FrameLayout
import android.widget.TextView
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.img_decorat.data.source.local.RoomTypeConverter
import com.example.img_decorat.ui.view.EditableImageView
import com.example.img_decorat.ui.view.TextImageView
import com.example.img_decorat.utils.Util.createDefaultBitmap

data class LayerItemData(
    val context: Context,
    var check : Boolean = false,
    var select : Boolean = false,
    val id : Int,
    var type: Int = 0,
    var bitMap : Bitmap
    = Bitmap.createBitmap(1024, 1024, Bitmap.Config.ARGB_8888).apply { (Color.TRANSPARENT) }
){
    var text: TextView = TextView(context)
}

data class ViewItemData(
    val context: Context,
    var visible : Boolean = true,
    val id : Int,
    var type : Int = 0,
    var saturation : Int = 0,
    var brightness : Int = 0,
    var transparency : Int = 100
){
    var img: EditableImageView = EditableImageView(context)
    var text: TextImageView = TextImageView(context)
}


data class EmojiList(
    val groupName : String,
    val groupList : MutableList<Bitmap>
)

data class LoadData(
    val titleImage: Bitmap,
    val title : String
)

@Entity(tableName = "emoji_list")
@TypeConverters(RoomTypeConverter::class)
data class EmojiDBData(
    @PrimaryKey val groupName : String,
    val groupList : List<String>
)

@Entity(tableName = "save_view_data")
@TypeConverters(RoomTypeConverter::class)
data class SaveViewData(
    @PrimaryKey
    val name : String,
    val data : List<SaveViewDataInfo>,
    val scale: Float = 1f,
    var bgColor : Int = Color.TRANSPARENT,
    var bgImg : String = "",
    val titleImage : String
)

data class SaveViewDataInfo(
    val type: Int,
    var visible : Boolean,
    val scale: Float,
    val rotationDegrees: Float,
    var saturationValue: Int,
    var brightnessValue: Int,
    var transparencyValue: Int,
    var matrixValue : FloatArray,
    val img: String = "",
    val text: String = "",
    val textSize : Int = 24,
    val textColor : Int = Color.TRANSPARENT,
    var bgColor : Int = Color.TRANSPARENT,
    val font : String = Typeface.DEFAULT.toString()
)

