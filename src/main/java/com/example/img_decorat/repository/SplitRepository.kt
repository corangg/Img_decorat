package com.example.img_decorat.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.widget.FrameLayout
import com.example.img_decorat.dataModels.ImageSize
import com.example.img_decorat.ui.view.SplitAreaView
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Stack
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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


    fun squareSplitView():SplitAreaView{
        val splitArea = SplitAreaView(context, type = 0).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setImageBitmap(layerListRepository.createTransparentBitmap(512,512))
        }
        return splitArea
    }

    fun cropImage(splitAreaView : SplitAreaView, bitmap: Bitmap) : Bitmap {


        val viewSize = splitAreaView.test()
        bitmap.width
        val scaleX = bitmap.width.toFloat()/viewSize.first.toFloat()
        val scaleY = bitmap.height.toFloat()/viewSize.second.toFloat()
        var left = 0f
        var top = 0f
        var right = 0f
        var bottom = 0f

        val rect = splitAreaView.getViewBoundsInParent()
        splitAreaView.test()


        if(scaleX>scaleY){
            val voidHeight = (scaleY*viewSize.second- bitmap.height)/2

            left = (rect.left*scaleY).coerceIn(0f, bitmap.width.toFloat())
            top = (rect.top*scaleY).coerceIn(0f, bitmap.height.toFloat()) - voidHeight
            right = (rect.right*scaleY).coerceIn(0f, bitmap.width.toFloat())
            bottom = (rect.bottom*scaleY).coerceIn(0f, bitmap.height.toFloat()) - voidHeight

        }else{
            val voidWith = (scaleY*viewSize.first - bitmap.width)/2

            left = (rect.left*scaleY).coerceIn(0f, bitmap.width.toFloat()) - voidWith
            top = (rect.top*scaleY).coerceIn(0f, bitmap.height.toFloat())
            right = (rect.right*scaleY).coerceIn(0f, bitmap.width.toFloat()) - voidWith
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

}