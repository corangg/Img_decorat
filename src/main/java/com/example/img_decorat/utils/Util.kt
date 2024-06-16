package com.example.img_decorat.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.presentation.ui.view.EditableImageView
import com.example.img_decorat.presentation.ui.view.TextImageView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.LinkedList

object Util {
    fun setLinearAdapter(
        recyclerView: RecyclerView,
        context: Context,
        adapter: RecyclerView.Adapter<*>,
        type: Int = 0
    ) {
        val linearLayoutManager = if (type == 0) {
            LinearLayoutManager.VERTICAL
        } else {
            LinearLayoutManager.HORIZONTAL
        }
        recyclerView.layoutManager = LinearLayoutManager(context, linearLayoutManager, false)
        recyclerView.adapter = adapter
    }

    fun setGridAdapter(
        recyclerView: RecyclerView,
        context: Context,
        spanCount: Int,
        adapter: RecyclerView.Adapter<*>,
        type: Int = 0
    ) {
        val gridLayoutManager = if (type == 0) {
            GridLayoutManager.VERTICAL
        } else {
            GridLayoutManager.HORIZONTAL
        }
        recyclerView.layoutManager = GridLayoutManager(context, spanCount, gridLayoutManager, false)
        recyclerView.adapter = adapter
    }

    fun buttonColorToggle(btn: ImageButton, firstColor: Int, secondColor: Int, toggle: Boolean) {
        if (toggle) {
            btn.backgroundTintList = ColorStateList.valueOf(firstColor)
        } else {
            btn.backgroundTintList = ColorStateList.valueOf(secondColor)
        }
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

    fun setID(): Int {
        return View.generateViewId()
    }

    fun getLastSelectView(viewList: LinkedList<ViewItemData>, id: Int): ViewItemData? {
        viewList.find { it.id == id }?.let {
            return it
        }
        return null
    }

    fun uriToBitmap(context: Context, imageUri: Uri): Bitmap? {
        var inputStream: InputStream? = null
        return try {
            inputStream = context.contentResolver.openInputStream(imageUri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }

    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


    fun createEditableImageView(
        context: Context,
        viewId: Int,
        bitmap: Bitmap,
        scale: Float = 1.0f,
        withSize: Int = FrameLayout.LayoutParams.WRAP_CONTENT,
        heightSize: Int = FrameLayout.LayoutParams.WRAP_CONTENT
    ): EditableImageView {
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
        context: Context,
        viewId: Int,
        textSize: Float = 24f,
        withSize: Int = FrameLayout.LayoutParams.WRAP_CONTENT,
        heightSize: Int = FrameLayout.LayoutParams.WRAP_CONTENT
    ): TextImageView {
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

    fun resizeBitmap(bitmap: Bitmap, size: Int): Pair<Bitmap, Float> {
        return if (bitmap.height > bitmap.width) {
            val scale = bitmap.height.toFloat() / bitmap.width.toFloat()
            val height = scale * size
            Pair(Bitmap.createScaledBitmap(bitmap, size, height.toInt(), true), scale)

        } else {
            val scale = bitmap.width.toFloat() / bitmap.height.toFloat()
            val width = scale * size
            Pair(Bitmap.createScaledBitmap(bitmap, width.toInt(), size, true), scale)
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
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = activity.currentFocus
        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}

