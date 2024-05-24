package com.example.img_decorat.data.source.local.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.img_decorat.data.model.dataModels.SaveViewData


@Dao
interface ViewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaveViewData(item: SaveViewData)

    @Query("SELECT * FROM save_view_data")
    suspend fun getSaveViewDataList(): List<SaveViewData>

    @Query("DELETE FROM save_view_data")
    suspend fun deleteAllsaveViewDataList()

    @Query("SELECT * FROM save_view_data WHERE name = :name")
    suspend fun getSaveViewDataByName(name: String): SaveViewData?

}

