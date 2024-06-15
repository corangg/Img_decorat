package com.example.img_decorat.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.example.img_decorat.data.model.dataModels.LoadData
import com.example.img_decorat.data.model.dataModels.SaveViewData
import com.example.img_decorat.domain.repository.SaveDataRepository
import com.example.img_decorat.utils.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SaveDataRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SaveDataRepository {
    override fun setLoadTitle(list: List<SaveViewData>): List<LoadData> {
        val loadDataList = mutableListOf<LoadData>()
        for (i in list) {
            val bitmap = Util.uriToBitmap(context, i.titleImage.toUri())
            bitmap?.let {
                val loadData = LoadData(
                    titleImage = it,
                    title = i.name
                )
                loadDataList.add(loadData)
            }
        }
        return loadDataList
    }
}