package com.example.img_decorat.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.img_decorat.utils.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class ConversionImageRepository @Inject constructor(
    @ApplicationContext private val context: Context) {
    fun bitmapToUri(bitmap: Bitmap): Uri? {
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

    fun uriToBitmap(imageUri: Uri): Bitmap? {
        context.contentResolver.openInputStream(imageUri).use { inputStream ->
            return BitmapFactory.decodeStream(inputStream)
        }
    }
}