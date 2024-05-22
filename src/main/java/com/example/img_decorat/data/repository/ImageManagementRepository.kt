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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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

        val file = File(folder, "$fileName.png")
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







}