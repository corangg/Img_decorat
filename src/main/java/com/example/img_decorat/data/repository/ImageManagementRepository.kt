package com.example.img_decorat.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.data.model.dataModels.SaveViewDataInfo
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.ui.view.EditableImageView
import com.example.img_decorat.ui.view.TextImageView
import com.example.img_decorat.utils.Util.bitmapToUri
import com.example.img_decorat.utils.Util.getBackgroundColor
import com.example.img_decorat.utils.Util.getBackgroundImage
import com.example.img_decorat.utils.Util.getBitmapFromView
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Singleton

@Singleton
class ImageManagementRepository(@ApplicationContext private val context: Context) {
    fun editViewSave(view: View, fileName: String) {
        val bitmap = getBitmapFromView(view)
        saveBitmapToFile(bitmap, fileName)
    }

    private fun saveBitmapToFile(bitmap: Bitmap, fileName: String) {
        val externalStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val folder = File(externalStorage, "IKKU")
        if (!folder.exists()) {
            folder.mkdirs()
        }

        var file = File(folder, "$fileName.png")
        var fileIndex = 1
        while (file.exists()) {
            file = File(folder, "${fileName}_$fileIndex.png")
            fileIndex++
        }
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null, null)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun saveView(list: List<ViewItemData>, view: FrameLayout, scale : Float, name: String):SaveViewData{
        val bitmap = getBackgroundImage(view)
        val titleBitmap = getBitmapFromView(view)

        if(bitmap != null){
            return SaveViewData(
                name = name,
                data = saveViewDataSet(list),
                scale = scale,
                bgColor = getBackgroundColor(view),
                bgImg = bitmapToUri(context,bitmap).toString(),
                titleImage = bitmapToUri(context,titleBitmap).toString()
            )
        }else{
            return SaveViewData(
                name = name,
                data = saveViewDataSet(list),
                scale = scale,
                bgColor = getBackgroundColor(view),
                titleImage = bitmapToUri(context,titleBitmap).toString()
            )
        }
    }

    fun saveViewDataSet(list : List<ViewItemData>):List<SaveViewDataInfo>{
        val saveViewDataInfoList : MutableList<SaveViewDataInfo> = mutableListOf()
        for(i in list){
            when(i.type){
                0->{
                    val img = i.img.getImageBitmap()
                    var uri : String = ""
                    img?.let {
                        uri = bitmapToUri(context,it).toString()
                    }
                    val mat = i.img.getMatrixValues()

                    val saveData = SaveViewDataInfo(
                        type = i.type,
                        visible = i.visible,
                        scale = i.img.scaleFactor,
                        rotationDegrees = i.img.rotationDegrees,
                        saturationValue = i.saturation,
                        brightnessValue = i.brightness,
                        transparencyValue = i.transparency,
                        matrixValue = i.img.getMatrixValues(),
                        img = uri
                    )
                    saveViewDataInfoList.add(saveData)
                }
                1->{
                    val saveData = SaveViewDataInfo(
                        type = i.type,
                        visible = i.visible,
                        scale = i.text.scaleFactor,
                        rotationDegrees = i.text.rotationDegrees,
                        saturationValue = i.saturation,
                        brightnessValue = i.brightness,
                        transparencyValue = i.transparency,
                        matrixValue = i.text.getMatrixValues(),
                        text = i.text.getTextContent(),
                        textSize = i.text.getTextSizeValue(),
                        textColor = i.text.getTextColor(),
                        bgColor = i.text.getBackgroundColor(),
                        font = i.text.getTextTypeface().toString()
                    )
                    saveViewDataInfoList.add(saveData)
                }
            }
        }
        return saveViewDataInfoList
    }
}