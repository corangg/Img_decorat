package com.example.img_decorat.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.widget.FrameLayout
import com.example.img_decorat.ui.view.SplitCircleView
import com.example.img_decorat.ui.view.SplitPolygonView
import com.example.img_decorat.ui.view.SplitSquareVIew
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Stack
import javax.inject.Inject

class SplitRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val layerListRepository: LayerListRepository) {
    val stackUndo = Stack<Bitmap>()
    val stackRun = Stack<Bitmap>()

    fun addUndoStack(bitmap: Bitmap?){
        if(bitmap!=null){
            stackUndo.push(bitmap)
        }
    }

    fun addRunStack(bitmap: Bitmap?){
        if(bitmap!=null){
            stackRun.push(bitmap)
        }
    }

    fun resetUndoStack(){
        stackUndo.clear()
    }
    fun resetRunStack(){
        stackRun.clear()
    }


    fun checkUndo():Boolean{
        return if(stackUndo.size>0){
            true
        }else{
            false
        }
    }
    fun checkRun():Boolean{
        return if(stackRun.size>0){
            true
        }else{
            false
        }
    }
    fun popUndo():Bitmap{
        return stackUndo.pop()
    }

    fun popRun():Bitmap{
        return stackRun.pop()
    }


    fun squareSplitView():SplitSquareVIew{
        val splitArea = SplitSquareVIew(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setImageBitmap(layerListRepository.createTransparentBitmap(512,512))
        }
        return splitArea
    }

    fun circleSplitView():SplitCircleView{
        val splitArea = SplitCircleView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setImageBitmap(layerListRepository.createTransparentBitmap(512,512))
        }
        return splitArea
    }

    fun polygoneSplitView():SplitPolygonView{
        val splitArea = SplitPolygonView(context, type = 0).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setImageBitmap(layerListRepository.createTransparentBitmap(512,512))
        }
        return splitArea
    }

    fun cropSquareImage(splitAreaView : SplitSquareVIew, bitmap: Bitmap) : Bitmap {
        val viewSize = splitAreaView.getParentSize()
        bitmap.width
        val scaleX = bitmap.width.toFloat()/viewSize.first.toFloat()
        val scaleY = bitmap.height.toFloat()/viewSize.second.toFloat()
        var left = 0f
        var top = 0f
        var right = 0f
        var bottom = 0f

        val rect = splitAreaView.getViewBoundsInParent()

        if(scaleX>scaleY){
            val voidHeight = (scaleX*viewSize.second- bitmap.height)/2

            left = (rect.left*scaleX).coerceIn(0f, bitmap.width.toFloat())
            top = (rect.top*scaleX - voidHeight).coerceIn(0f, bitmap.height.toFloat())
            right = (rect.right*scaleX).coerceIn(0f, bitmap.width.toFloat())
            bottom = (rect.bottom*scaleX - voidHeight).coerceIn(0f, bitmap.height.toFloat())

        }else{
            val voidWith = (scaleY*viewSize.first - bitmap.width)/2

            left = (rect.left*scaleY- voidWith).coerceIn(0f, bitmap.width.toFloat())
            top = (rect.top*scaleY).coerceIn(0f, bitmap.height.toFloat())
            right = (rect.right*scaleY- voidWith).coerceIn(0f, bitmap.width.toFloat())
            bottom = (rect.bottom*scaleY).coerceIn(0f, bitmap.height.toFloat())
        }
        val croppedBitmap = Bitmap.createBitmap(
            bitmap,
            left.toInt(),
            top.toInt(),
            (right - left).toInt(),
            (bottom - top).toInt()
        )

        return croppedBitmap
    }

    fun cropCircleImage(circleView: SplitCircleView,bitmap: Bitmap):Bitmap{
        val circlePath = Path()
        val pos = circleView.getCurrentImagePosition()
        val centerX = pos.first
        val centerY = pos.second
        val radius = circleView.radius

        circlePath.addCircle(centerX, centerY, radius, Path.Direction.CW)

        val viewSize = circleView.getParentSize()
        val scaleX = viewSize.first.toFloat() / bitmap.width.toFloat()
        val scaleY = viewSize.second.toFloat() / bitmap.height.toFloat()
        var scale = 0f
        var offsetX = 0f
        var offsetY = 0f

        if (scaleX < scaleY) {
            offsetY = (viewSize.second - scaleX * bitmap.height) / 2
            scale = scaleX
        } else {
            offsetX = (viewSize.first - scaleY * bitmap.width) / 2
            scale = scaleY
        }

        val matrix = Matrix()
        matrix.setScale(scale, scale)
        matrix.postTranslate(offsetX, offsetY)

        val scaledBitmap = Bitmap.createBitmap(viewSize.first, viewSize.second, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(scaledBitmap)

        val paint = Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
        }

        canvas.drawPath(circlePath, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        val srcRect = Rect(0, 0, bitmap.width, bitmap.height)
        val destRect = Rect(offsetX.toInt(), offsetY.toInt(), (bitmap.width * scale + offsetX).toInt(), (bitmap.height * scale + offsetY).toInt())
        canvas.drawBitmap(bitmap, srcRect, destRect, paint)

        return scaledBitmap
    }

    fun cropPolygonImage(splitAreaView: SplitPolygonView, bitmap: Bitmap): Bitmap {
        val polygonPath = splitAreaView.getPolygonPath()
        val viewSize = splitAreaView.getParentSize()
        val scaleX = viewSize.first.toFloat()/bitmap.width.toFloat()
        val scaleY = viewSize.second.toFloat()/bitmap.height.toFloat()
        var scale = 0f
        var offsetX = 0f
        var offsetY = 0f

        if(scaleX<scaleY){
            offsetY = (viewSize.second- scaleX*bitmap.height)/2
            scale = scaleX

        }else{
            offsetX = (viewSize.first - scaleY*bitmap.width)/2
            scale = scaleY
        }

        val matrix = Matrix()
        matrix.setScale(scale, scale)
        matrix.postTranslate(offsetX, offsetY)
        val scaledPath = Path()
        polygonPath.transform(matrix, scaledPath)

        val scaledBitmap = Bitmap.createBitmap(viewSize.first, viewSize.second, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(scaledBitmap)

        val paint = Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
        }

        canvas.drawPath(polygonPath, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        val srcRect = Rect(0, 0, bitmap.width, bitmap.height)

        val destRect = Rect(offsetX.toInt(), offsetY.toInt(), (bitmap.width * scale + offsetX).toInt(), (bitmap.height * scale + offsetY).toInt())
        canvas.drawBitmap(bitmap, srcRect, destRect, paint)

        return scaledBitmap
    }

}