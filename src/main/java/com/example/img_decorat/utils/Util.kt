package com.example.img_decorat.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.widget.FrameLayout
import androidx.core.content.FileProvider
import com.example.img_decorat.ui.view.EditableImageView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object Util{
    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
        val file = File(context.cacheDir, "${System.currentTimeMillis()}.png")
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun uriToBitmap(context: Context, imageUri: Uri): Bitmap? {
        context.contentResolver.openInputStream(imageUri).use { inputStream ->
            return BitmapFactory.decodeStream(inputStream)
        }
    }

    fun createEditableImageView(
        context : Context,
        viewId :Int,
        bitmap: Bitmap,
        scale : Float = 1.0f,
        withSize : Int = FrameLayout.LayoutParams.WRAP_CONTENT,
        heightSize : Int = FrameLayout.LayoutParams.WRAP_CONTENT): EditableImageView{
        val imageView = EditableImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                withSize,
                heightSize
            )
            id = viewId
            setImageBitmap(bitmap)
        }
        imageView.setImageScale(scale)

        return imageView
    }

    fun resizeBitmap(bitmap: Bitmap,size: Int): Pair<Bitmap,Float>{
        return if(bitmap.height > bitmap.width){
            val scale = bitmap.height.toFloat()/bitmap.width.toFloat()
            val height = scale*size
            Pair(Bitmap.createScaledBitmap(bitmap,size,height.toInt(),true),scale)

        }else{
            val scale = bitmap.width.toFloat()/bitmap.height.toFloat()
            val width = scale*size
            Pair(Bitmap.createScaledBitmap(bitmap,width.toInt(),size,true),scale)
        }
    }



}

