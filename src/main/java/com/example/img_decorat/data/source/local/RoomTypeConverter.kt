package com.example.img_decorat.data.source.local

import android.app.WallpaperColors.fromBitmap
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class RoomTypeConverter {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun fromBitmapList(bitmapList: List<Bitmap>): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(bitmapList.map { fromBitmap(it) })
        objectOutputStream.close()
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
    }

    @TypeConverter
    fun toBitmapList(data: String): MutableList<Bitmap> {
        val byteArrayInputStream = ByteArrayInputStream(Base64.decode(data, Base64.DEFAULT))
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        @Suppress("UNCHECKED_CAST")
        val byteArrayList = objectInputStream.readObject() as List<ByteArray>
        objectInputStream.close()
        return byteArrayList.map { toBitmap(it) }.toMutableList()
    }
}