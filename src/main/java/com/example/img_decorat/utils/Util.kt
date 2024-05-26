package com.example.img_decorat.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import com.example.img_decorat.ui.view.EditableImageView
import com.example.img_decorat.ui.view.TextImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

object Util{
    fun createDefaultBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888)
        return bitmap
    }
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

    fun setID() : Int{
        val id =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                View.generateViewId()
            } else {
                ViewCompat.generateViewId()
            }
        return id
    }

    fun stringToTypeface(fontName: String): Typeface {
        return Typeface.create(fontName, Typeface.NORMAL)
    }

    fun uriToBitmap(context: Context, imageUri: Uri): Bitmap? {
        context.contentResolver.openInputStream(imageUri).use { inputStream ->
            return BitmapFactory.decodeStream(inputStream)
        }
    }

    fun getBitmapFromView(view: View): Bitmap{
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun FloatArray.toMatrix(): Matrix {
        if (this.size != 9) throw IllegalArgumentException("Array must have exactly 9 elements to convert to a Matrix")
        return Matrix().apply { setValues(this@toMatrix) }
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

    fun createEditableTextView(
        context : Context,
        viewId :Int,
        textSize : Float = 24f,
        withSize : Int = FrameLayout.LayoutParams.WRAP_CONTENT,
        heightSize : Int = FrameLayout.LayoutParams.WRAP_CONTENT): TextImageView{
        val editView = TextImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                withSize,
                heightSize
            )
            id = viewId
            setTextSize(textSize)
        }

        return editView
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

    fun getBackgroundColor(view: View): Int {
        if (view.background is ColorDrawable) {
            return (view.background as ColorDrawable).color
        }
        return Color.TRANSPARENT
    }

    fun getBackgroundImage(view: View): Bitmap? {
        val drawable = view.background ?: return null
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        if (width <= 0 || height <= 0) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }




}

