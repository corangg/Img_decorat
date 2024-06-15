package com.example.img_decorat.data.model.dataModels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.TextView
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.img_decorat.data.source.local.RoomTypeConverter
import com.example.img_decorat.domain.usecase.emojiusecase.EmojiAddLayerUseCase
import com.example.img_decorat.domain.usecase.emojiusecase.EmojiDBListClassificationUseCase
import com.example.img_decorat.domain.usecase.emojiusecase.EmojiDataStringToBitmapUseCase
import com.example.img_decorat.domain.usecase.imagemanagementusecase.EditViewUseCase
import com.example.img_decorat.domain.usecase.imagemanagementusecase.SaveViewUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.CheckLastSelectImageUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.CheckedListUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.CheckedViewTypeUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.DeleteListUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.ImageAddListUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.LoadListUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.SelectLastImageUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.SelectLayerUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.SetLastTouchedImageUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.SplitImageChangeListUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.SwapImageViewUseCase
import com.example.img_decorat.presentation.ui.view.EditableImageView
import com.example.img_decorat.presentation.ui.view.TextImageView
import java.util.LinkedList

data class ListData(
    var layerList : LinkedList<LayerItemData>,
    var viewList : LinkedList<ViewItemData>
)

data class Hue(
    val saturatio: Int,
    val brightness: Int,
    val transparency: Int
)


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
    val font : Int = -1
)

data class LayerListUseCases(
    val checkedListUseCase: CheckedListUseCase,
    val checkedViewTypeUseCase: CheckedViewTypeUseCase,
    val checkLastSelectImageUseCase: CheckLastSelectImageUseCase,
    val deleteListUseCase: DeleteListUseCase,
    val imageAddListUseCase: ImageAddListUseCase,
    val loadListUseCase: LoadListUseCase,
    val selectLastImageUseCase: SelectLastImageUseCase,
    val selectLayerUseCase: SelectLayerUseCase,
    val setLastTouchedImageUseCase: SetLastTouchedImageUseCase,
    val splitImageChangeListUseCase: SplitImageChangeListUseCase,
    val swapImageViewUseCase: SwapImageViewUseCase
)

data class ImageManagementUseCases(
    val editViewUseCase: EditViewUseCase,
    val saveViewUseCase: SaveViewUseCase
)

data class EmojiUseCases(
    val emojiAddLayerUseCase: EmojiAddLayerUseCase,
    val emojiDataStringToBitmapUseCase: EmojiDataStringToBitmapUseCase,
    val emojiDBListClassificationUseCase: EmojiDBListClassificationUseCase
)

