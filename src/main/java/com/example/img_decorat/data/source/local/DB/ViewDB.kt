package com.example.img_decorat.data.source.local.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.data.source.local.Dao.ViewDao
import com.example.img_decorat.data.source.local.RoomTypeConverter

/*@Database(entities = [SaveViewData::class], version = 1, exportSchema = false)
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
}*/

@Database(entities = [SaveViewData::class], version = 2, exportSchema = false)
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
                )
                    .addMigrations(MIGRATION_1_2) // 마이그레이션 추가
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 임시 테이블 생성
                database.execSQL("""
                    CREATE TABLE save_view_data_new (
                        name TEXT PRIMARY KEY NOT NULL,
                        data TEXT NOT NULL,
                        scale REAL NOT NULL,
                        bgColor INTEGER NOT NULL,
                        bgImg TEXT NOT NULL,
                        titleImage TEXT NOT NULL
                    )
                """.trimIndent())

                // 데이터 복사
                database.execSQL("""
                    INSERT INTO save_view_data_new (name, data, scale, bgColor, bgImg, titleImage)
                    SELECT name, data, scale, bgColor, bgImg, titleImage FROM save_view_data
                """.trimIndent())

                // 기존 테이블 삭제
                database.execSQL("DROP TABLE save_view_data")

                // 테이블 이름 변경
                database.execSQL("ALTER TABLE save_view_data_new RENAME TO save_view_data")

                // 데이터를 처리하여 font 필드를 Int로 변환 (이 부분은 필요한 경우 추가적인 처리가 필요합니다)
            }
        }
    }
}
