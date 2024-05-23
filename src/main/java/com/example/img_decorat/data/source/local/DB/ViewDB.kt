package com.example.img_decorat.data.source.local.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.data.source.local.Dao.ViewDao
import com.example.img_decorat.data.source.local.RoomTypeConverter

@Database(entities = [SaveViewData::class], version = 1, exportSchema = false)
@TypeConverters(RoomTypeConverter::class)
abstract class ViewDB : RoomDatabase() {

    abstract fun viewDao(): ViewDao

    companion object {
        @Volatile
        private var INSTANCE: ViewDB? = null

        fun getDatabase(context: Context): ViewDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ViewDB::class.java,
                    "view_data"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
