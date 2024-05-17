package com.example.img_decorat.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

/*
abstract class ZoomableView@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : AppCompatImageView(context, attrs, defStyle), View.OnTouchListener {

    protected val matrix = Matrix()
    protected var scaleFactor = 1.0f
    protected var lastTouchX = 0f
    protected var lastTouchY = 0f
    protected var parentWidth = 0
    protected var parentHeight = 0


    init {
        setOnTouchListener(this)
        scaleType = ScaleType.MATRIX
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        parentWidth = (parent as View).width
        parentHeight = (parent as View).height
    }


    fun strokePaint(strokeColor: Int, thickness : Float):Paint{
        return Paint().apply {
            color = strokeColor
            style = Paint.Style.STROKE
            strokeWidth = thickness}
    }

    fun getParentSize():Pair<Int,Int>{
        return Pair(parentWidth,parentHeight)
    }

    fun getCurrentImageSize(): Pair<Float, Float> {
        val values = FloatArray(9)
        matrix.getValues(values)
        val scaleX = values[Matrix.MSCALE_X]
        val scaleY = values[Matrix.MSCALE_Y]

        val drawable = drawable ?: return Pair(0f, 0f)
        val originalWidth = drawable.intrinsicWidth
        val originalHeight = drawable.intrinsicHeight

        return Pair(originalWidth * scaleX, originalHeight * scaleY)
    }

    fun getCurrentImagePosition(): Pair<Float, Float> {
        val values = FloatArray(9)
        matrix.getValues(values)
        val transX = values[Matrix.MTRANS_X]
        val transY = values[Matrix.MTRANS_Y]
        return Pair(transX, transY)
    }
}*/
