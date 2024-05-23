package com.example.img_decorat.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
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

    fun deserializeView(viewDataString: String, parent: ViewGroup, context: Context): View {
        val viewData = Gson().fromJson<Map<String, Any>>(viewDataString, object : TypeToken<Map<String, Any>>() {}.type)
        val viewType = viewData["type"] as String

        val view: View = when (viewType) {
            "TextView" -> {
                val textView = TextView(context)
                textView.text = viewData["text"] as String
                textView
            }
            "EditableImageView" -> {
                val editableImageView = EditableImageView(context)
                editableImageView.scaleFactor = (viewData["scaleFactor"] as Double).toFloat()
                editableImageView.rotationDegrees = (viewData["rotationDegrees"] as Double).toFloat()
                editableImageView.saturationValue = (viewData["saturationValue"] as Double).toFloat()
                editableImageView.brightnessValue = (viewData["brightnessValue"] as Double).toFloat()

                val matrixValues = (viewData["matrixValues"] as List<Double>).map { it.toFloat() }.toFloatArray()
                editableImageView.matrix.setValues(matrixValues)
                editableImageView.imageMatrix = editableImageView.matrix
                editableImageView
            }
            "TextImageView" -> {
                val textImageView = TextImageView(context)
                textImageView.scaleFactor = (viewData["scaleFactor"] as Double).toFloat()
                textImageView.rotationDegrees = (viewData["rotationDegrees"] as Double).toFloat()
                textImageView.saturationValue = (viewData["saturationValue"] as Double).toFloat()
                textImageView.brightnessValue = (viewData["brightnessValue"] as Double).toFloat()
                //textImageView.text = viewData["text"] as String
                textImageView.fillBackgroundPaint.color = (viewData["fillBackgroundColor"] as Double).toInt()

                val matrixValues = (viewData["matrixValues"] as List<Double>).map { it.toFloat() }.toFloatArray()
                textImageView.matrix.setValues(matrixValues)
                textImageView
            }
            "FrameLayout" -> {
                val frameLayout = FrameLayout(context)
                val children = viewData["children"] as List<Map<String, Any>>
                for (childData in children) {
                    val childView = deserializeView(Gson().toJson(childData), frameLayout, context)
                    frameLayout.addView(childView)
                }
                frameLayout
            }
            else -> {
                throw IllegalArgumentException("Unknown view type: $viewType")
            }
        }

        view.alpha = (viewData["alpha"] as Double).toFloat()
        parent.addView(view)
        return view
    }


}

