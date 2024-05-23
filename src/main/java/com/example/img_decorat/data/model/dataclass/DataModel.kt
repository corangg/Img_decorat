package com.example.img_decorat.data.model.dataModels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.TextView
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.img_decorat.data.source.local.RoomTypeConverter
import com.example.img_decorat.ui.view.EditableImageView
import com.example.img_decorat.ui.view.TextImageView

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

@Entity(tableName = "emoji_data")
@TypeConverters(RoomTypeConverter::class)
data class EmojiList(
    @PrimaryKey val groupName : String,
    val groupList : MutableList<Bitmap>
)
