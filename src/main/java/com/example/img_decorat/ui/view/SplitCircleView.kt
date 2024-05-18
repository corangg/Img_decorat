package com.example.img_decorat.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide.init

class SplitCircleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle), View.OnTouchListener {
    private val matrix = Matrix()
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private var scaleFactor = 1.0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var parentWidth = 0
    private var parentHeight = 0

    var radius: Float = 200f
        set(value) {
            field = value
            invalidate()
        }

    init {
        setOnTouchListener(this)
        scaleType = ScaleType.MATRIX
    }

    fun getParentSize():Pair<Int,Int>{
        return Pair(parentWidth,parentHeight)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        parentWidth = (parent as View).width
        parentHeight = (parent as View).height
    }

    fun strokePaint(strokeColor: Int, thickness : Float): Paint {
        return Paint().apply {
            color = strokeColor
            style = Paint.Style.STROKE
            strokeWidth = thickness}
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)
    }

    private fun drawBorder(canvas: Canvas) {
        val pos = getCurrentImagePosition()
        canvas.drawCircle(pos.first,pos.second, radius, strokePaint(Color.WHITE,4f))
    }
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if(!isTouchInsideCircle(event)){
            return false
        }

        scaleGestureDetector.onTouchEvent(event)

        if (!scaleGestureDetector.isInProgress) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.x
                    lastTouchY = event.y
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY

                    matrix.postTranslate(dx, dy)
                    imageMatrix = matrix
                    lastTouchX = event.x
                    lastTouchY = event.y
                    invalidate()
                }
            }
        }
        return true
    }

    fun isTouchInsideCircle(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        val pos = getCurrentImagePosition()
        val centerX = pos.first
        val centerY = pos.second
        val distance = Math.sqrt(Math.pow((x - centerX).toDouble(), 2.0) + Math.pow((y - centerY).toDouble(), 2.0))
        return distance <= radius
    }

    fun getCurrentImagePosition(): Pair<Float, Float> {
        val values = FloatArray(9)
        matrix.getValues(values)
        val transX = values[Matrix.MTRANS_X]
        val transY = values[Matrix.MTRANS_Y]
        return Pair(transX, transY)
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var scale = detector.scaleFactor
            val currentScaleFactor = scaleFactor * scale
            val maxScale = 6f
            val minScale = 0.1f
            if (currentScaleFactor > maxScale) {
                scale = maxScale / scaleFactor
            } else if (currentScaleFactor < minScale) {
                scale = minScale / scaleFactor
            }
            radius *= scale
            invalidate()
            return true
        }
    }
}