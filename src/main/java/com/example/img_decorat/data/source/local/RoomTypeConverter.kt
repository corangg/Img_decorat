package com.example.img_decorat.data.source.local

import android.app.WallpaperColors.fromBitmap
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import com.example.img_decorat.data.model.dataModels.SaveViewDataInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class RoomTypeConverter {
    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toStringList(data: String?): List<String>? {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun fromSaveViewDataInfoList(value: List<SaveViewDataInfo>?): String {
        val gson = Gson()
        val type = object : TypeToken<List<SaveViewDataInfo>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSaveViewDataInfoList(value: String): List<SaveViewDataInfo>? {
        val gson = Gson()
        val type = object : TypeToken<List<SaveViewDataInfo>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromFloatArray(value: FloatArray): String {
        return value.joinToString(separator = ",")
    }

    @TypeConverter
    fun toFloatArray(value: String): FloatArray {
        return value.split(",").map { it.toFloat() }.toFloatArray()
    }


}