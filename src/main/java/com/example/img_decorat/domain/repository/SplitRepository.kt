package com.example.img_decorat.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.example.img_decorat.presentation.ui.view.SplitCircleView
import com.example.img_decorat.presentation.ui.view.SplitPolygonView
import com.example.img_decorat.presentation.ui.view.SplitSquareView

interface SplitRepository {
    fun getIntentBitmap(uri: Uri): Bitmap?

    fun setIntentUri(bitmap: Bitmap): Uri?

    fun squareSplitView(): SplitSquareView

    fun circleSplitView(): SplitCircleView

    fun polygoneSplitView(): SplitPolygonView

    fun cropSquareImage(splitAreaView: SplitSquareView, bitmap: Bitmap): Bitmap

    fun cropCircleImage(circleView: SplitCircleView, bitmap: Bitmap): Bitmap

    fun cropPolygonImage(splitAreaView: SplitPolygonView, bitmap: Bitmap): Bitmap
}