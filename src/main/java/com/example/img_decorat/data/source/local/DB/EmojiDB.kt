package com.example.img_decorat.data.source.local.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.img_decorat.data.model.dataModels.EmojiDBData
import com.example.img_decorat.data.model.dataModels.EmojiList
import com.example.img_decorat.data.source.local.Dao.EmojiDao
import com.example.img_decorat.data.source.local.RoomTypeConverter

@Database(entities = [EmojiDBData::class], version = 1, exportSchema = false)
@TypeConverters(RoomTypeConverter::class)
abstract class EmojiDB : RoomDatabase() {

    abstract fun emojiDao(): EmojiDao

    companion object {
        @Volatile
        private var INSTANCE: EmojiDB? = null

        fun getDatabase(context: Context): EmojiDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmojiDB::class.java,
                    "emojilist_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}