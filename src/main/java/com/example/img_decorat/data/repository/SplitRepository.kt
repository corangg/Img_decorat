package com.example.img_decorat.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.net.Uri
import android.widget.FrameLayout
import com.example.img_decorat.presentation.ui.view.SplitCircleView
import com.example.img_decorat.presentation.ui.view.SplitPolygonView
import com.example.img_decorat.presentation.ui.view.SplitSquareView
import com.example.img_decorat.utils.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SplitRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val layout = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.MATCH_PARENT
    )

    fun getIntentBitmap(uri: Uri): Bitmap? {
        return Util.uriToBitmap(context = context, imageUri = uri)
    }

    fun setIntentUri(bitmap: Bitmap): Uri? {
        return Util.bitmapToUri(context = context, bitmap = bitmap)
    }


    fun squareSplitView(): SplitSquareView {
        val splitArea = SplitSquareView(context).apply {
            layoutParams = layout
            setImageBitmap(createTransparentBitmap(512, 512))//얜 이게 왜 필요하냐???ㅅㅅㅂ
        }
        return splitArea
    }

    fun createTransparentBitmap(width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.TRANSPARENT)
        }
    }

    fun circleSplitView(): SplitCircleView {
        val splitArea = SplitCircleView(context).apply {
            layoutParams = layout
        }
        return splitArea
    }

    fun polygoneSplitView(): SplitPolygonView {
        val splitArea = SplitPolygonView(context, type = 0).apply {
            layoutParams = layout
        }
        return splitArea
    }

    fun cropSquareImage(splitAreaView: SplitSquareView, bitmap: Bitmap): Bitmap {
        val viewSize = splitAreaView.getParentSize()
        val areaPoints = splitAreaView.areaPoint()
        val path = Path().apply {
            moveTo(areaPoints[0], areaPoints[1])
            lineTo(areaPoints[2], areaPoints[1])
            lineTo(areaPoints[2], areaPoints[3])
            lineTo(areaPoints[0], areaPoints[3])
            close()
        }
        val (scale, offsetX, offsetY) = calculateScaleAndOffset(viewSize, bitmap)

        return createScaledBitmap(bitmap, viewSize, path, scale, offsetX, offsetY)
    }

    fun cropCircleImage(circleView: SplitCircleView, bitmap: Bitmap): Bitmap {
        val (centerX, centerY) = circleView.getCurrentImagePosition()
        val radius = circleView.radius
        val circlePath = Path().apply {
            addCircle(centerX, centerY, radius, Path.Direction.CW)
        }
        val viewSize = circleView.getParentSize()
        val (scale, offsetX, offsetY) = calculateScaleAndOffset(viewSize, bitmap)

        return createScaledBitmap(bitmap, viewSize, circlePath, scale, offsetX, offsetY)
    }

    fun cropPolygonImage(splitAreaView: SplitPolygonView, bitmap: Bitmap): Bitmap {
        val polygonPath = splitAreaView.getPolygonPath()
        val viewSize = splitAreaView.getParentSize()
        val (scale, offsetX, offsetY) = calculateScaleAndOffset(viewSize, bitmap)

        return createScaledBitmap(bitmap, viewSize, polygonPath, scale, offsetX, offsetY)
    }

    fun calculateScaleAndOffset(
        viewSize: Pair<Int, Int>,
        bitmap: Bitmap
    ): Triple<Float, Float, Float> {
        val scaleX = viewSize.first.toFloat() / bitmap.width
        val scaleY = viewSize.second.toFloat() / bitmap.height
        val scale: Float
        val offsetX: Float
        val offsetY: Float

        if (scaleX < scaleY) {
            scale = scaleX
            offsetY = (viewSize.second - scale * bitmap.height) / 2
            offsetX = 0f
        } else {
            scale = scaleY
            offsetX = (viewSize.first - scale * bitmap.width) / 2
            offsetY = 0f
        }

        return Triple(scale, offsetX, offsetY)
    }

    fun createScaledBitmap(
        bitmap: Bitmap,
        viewSize: Pair<Int, Int>,
        path: Path,
        scale: Float,
        offsetX: Float,
        offsetY: Float
    ): Bitmap {
        val scaledBitmap =
            Bitmap.createBitmap(viewSize.first, viewSize.second, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(scaledBitmap)

        val paint = Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
        }
        val srcRect = Rect(0, 0, bitmap.width, bitmap.height)
        val destRect = Rect(
            offsetX.toInt(),
            offsetY.toInt(),
            (bitmap.width * scale + offsetX).toInt(),
            (bitmap.height * scale + offsetY).toInt()
        )
        canvas.drawBitmap(bitmap, srcRect, destRect, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        val inversePath = Path()
        inversePath.addRect(
            0f,
            0f,
            viewSize.first.toFloat(),
            viewSize.second.toFloat(),
            Path.Direction.CW
        )
        inversePath.op(path, Path.Op.DIFFERENCE)
        canvas.drawPath(inversePath, paint)

        return scaledBitmap
    }
}