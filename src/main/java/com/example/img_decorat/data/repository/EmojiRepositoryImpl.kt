package com.example.img_decorat.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.example.img_decorat.data.model.dataModels.EmojiDBData
import com.example.img_decorat.data.model.dataModels.EmojiData
import com.example.img_decorat.data.model.dataModels.EmojiList
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.domain.repository.EmojiRepository
import com.example.img_decorat.domain.usecase.layerlistusecase.ImageAddViewUseCase
import com.example.img_decorat.utils.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmojiRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageAddViewUseCase: ImageAddViewUseCase
) : EmojiRepository {
    override fun emojiDataStringToBitmap(list: List<EmojiDBData>): List<EmojiList> {
        val transformationList = mutableListOf<EmojiList>()
        for (i in list) {
            val groupName = i.groupName
            val emojiList = mutableListOf<Bitmap>()
            for (j in i.groupList) {
                emojiList.add(createBitmapFromEmoji(j))
            }
            transformationList.add(EmojiList(groupName, emojiList))
        }
        return transformationList
    }

    override fun emojiDBListClassification(list: List<EmojiData>): List<EmojiDBData> {
        val classification = mutableListOf<EmojiDBData>()
        var groupName = list[0].group
        var emojiBitmapList = mutableListOf<String>()

        for (i in list) {
            if (i.group != groupName) {
                val emojiGroup = EmojiDBData(groupName = groupName, groupList = emojiBitmapList)
                classification.add(emojiGroup)
                groupName = i.group
                emojiBitmapList = mutableListOf()
                emojiBitmapList.add(i.character)
            } else {
                emojiBitmapList.add(i.character)
            }
        }
        val emojiGroup = EmojiDBData(groupName = groupName, groupList = emojiBitmapList)
        classification.add(emojiGroup)
        return classification
    }

    override fun emojiAddLayer(listData: ListData, viewSize: Int, bitmap: Bitmap): ListData {
        val id = Util.setID()
        val resizeBitmap = Util.resizeBitmap(bitmap, viewSize).first
        val layerItemData =
            LayerItemData(context = context, check = true, id = id, bitMap = resizeBitmap)
        listData.layerList.add(layerItemData)
        imageAddViewUseCase.execute(listData.viewList, id, resizeBitmap, true, 0.4f)?.let {
            listData.viewList = it
        }
        return listData
    }

    private fun createBitmapFromEmoji(emoji: String): Bitmap {
        val paint = Paint()
        paint.textSize = 200f
        paint.isAntiAlias = true
        paint.textAlign = Paint.Align.CENTER

        val baseline = -paint.ascent()
        val width = (paint.measureText(emoji) + 0.5f).toInt()
        val height = (baseline + paint.descent() + 0.5f).toInt()

        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawText(emoji, (width / 2).toFloat(), baseline, paint)

        return image
    }
}