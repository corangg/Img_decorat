package com.example.img_decorat.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.example.img_decorat.viewmodel.MainViewModel

class EditableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle), View.OnTouchListener {

    private val matrix = Matrix()
    private var scaleFactor = 1.0f
    private var rotationDegrees = 0f
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private val rotateGestureDetector = RotateGestureDetector(RotateListener())
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var saturationValue = 0f
    private var brightnessValue = 0f
    private val selectBorderPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }
    private val unSelectBorderPaint = Paint().apply {
        color = Color.TRANSPARENT
        style = Paint.Style.STROKE
        strokeWidth = 0f
    }
    private var viewModel: MainViewModel? = null

    init {
        setOnTouchListener(this)
        scaleType = ScaleType.MATRIX
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val transPos = getTransformedPoints()

        if (!isPointInPolygon(event.x,event.y,transPos)) {
            return false
        }

        scaleGestureDetector.onTouchEvent(event)
        rotateGestureDetector.onTouchEvent(event)

        if (!scaleGestureDetector.isInProgress && !rotateGestureDetector.isInProgress) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.x
                    lastTouchY = event.y
                    viewModel?.selectLastImage(this.id)
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)
    }

    fun setViewModel(viewModel: MainViewModel) {
        this.viewModel = viewModel
    }

    private fun isPointInPolygon(x: Float, y: Float, polygon: FloatArray): Boolean {
        var intersectCount = 0
        for (i in polygon.indices step 2) {
            val x1 = polygon[i]
            val y1 = polygon[i + 1]
            val x2 = polygon[(i + 2) % polygon.size]
            val y2 = polygon[(i + 3) % polygon.size]

            if (((y1 > y) != (y2 > y)) &&
                (x < (x2 - x1) * (y - y1) / (y2 - y1) + x1)
            ) {
                intersectCount++
            }
        }
        return (intersectCount % 2) == 1
    }

    private fun drawBorder(canvas: Canvas) {
        val points = getTransformedPoints()
        val path = Path().apply {
            moveTo(points[0], points[1])
            lineTo(points[2], points[3])
            lineTo(points[4], points[5])
            lineTo(points[6], points[7])
            close()
        }
        if (viewModel?.lastTouchedImageId?.value == this.id) {
            canvas.drawPath(path, selectBorderPaint)
        } else {
            canvas.drawPath(path, unSelectBorderPaint)
        }
    }

    fun getTransformedPoints(): FloatArray {
        val drawable = drawable ?: return floatArrayOf()
        val width = drawable.intrinsicWidth.toFloat()
        val height = drawable.intrinsicHeight.toFloat()
        val points = floatArrayOf(
            0f, 0f,
            width, 0f,
            width, height,
            0f, height
        )
        matrix.mapPoints(points)
        return points
    }

    fun setImageTransparency(alpha: Float) {
        val clampedAlpha = Math.max(0f, Math.min(alpha, 100f)) / 100f
        this.alpha = clampedAlpha
        invalidate()
    }

    fun setImageSaturation(saturation: Float) {
        /*val saturationShame = (saturation + 100f)/100f

        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(saturationShame)
        val filter = ColorMatrixColorFilter(colorMatrix)
        colorFilter = filter

        invalidate()*/
        saturationValue = (saturation + 100f)/100f
    }

    fun setImageBrightness(brightness: Float) {
        //val clampedBrightness = Math.max(-100f, Math.min(brightness, 100f))
        val scale = (brightness + 100f) / 100f // -100 to 100 -> 0 to 2
        val translate = -128f * (scale - 1)

        val colorMatrix = ColorMatrix()
        colorMatrix.set(arrayOf(
            scale, 0f, 0f, 0f, translate,
            0f, scale, 0f, 0f, translate,
            0f, 0f, scale, 0f, translate,
            0f, 0f, 0f, 1f, 0f
        ).toFloatArray())

        val filter = ColorMatrixColorFilter(colorMatrix)
        colorFilter = filter

        invalidate()
    }


    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scale = detector.scaleFactor
            scaleFactor *= scale
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f))

            matrix.postScale(scale, scale, detector.focusX, detector.focusY)
            imageMatrix = matrix
            invalidate()
            return true
        }
    }

    private inner class RotateListener : RotateGestureDetector.OnRotateGestureListener {
        override fun onRotate(detector: RotateGestureDetector): Boolean {
            val rotation = detector.rotationDegreesDelta
            rotationDegrees += rotation
            matrix.postRotate(rotation, detector.focusX, detector.focusY)
            imageMatrix = matrix
            invalidate()
            return true
        }
    }
}