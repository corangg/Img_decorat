package com.example.img_decorat.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

class SplitPolygonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, type : Int
) : AppCompatImageView(context, attrs, defStyle), View.OnTouchListener {

    private val matrix = Matrix()
    var polygonPoints : Int = 3
    private var scaleFactor = 1.0f
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var parentWidth = 0
    private var parentHeight = 0
    private val dotRadius = 16f

    private var pointsArray = floatArrayOf()
    private var movingPointIndex = -1


    init {
        setOnTouchListener(this)
        scaleType = ScaleType.MATRIX
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        parentWidth = (parent as View).width
        parentHeight = (parent as View).height
    }

    fun setPolygon(polygone: Int){
        polygonPoints = polygone
    }

    fun touchPoint(event: MotionEvent){
        val x = event.x
        val y = event.y

        for(i in pointsArray.indices step 2 ){
            if(x in pointsArray[i]-20 .. pointsArray[i]+20&& y in pointsArray[i+1]-20 .. pointsArray[i+1] +20){
                true

            }
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)
        invalidate()
    }
    fun strokePaint(strokeColor: Int, thickness : Float):Paint{
        return Paint().apply {
            color = strokeColor
            style = Paint.Style.STROKE
            strokeWidth = thickness}
    }

    private fun drawBorder(canvas: Canvas) {
        val points = getPolygonPoints()
        val path = Path().apply {
            moveTo(points[0], points[1])
            for (i in 2 until points.size step 2) {
                lineTo(points[i], points[i + 1])
            }
            close()
        }
        canvas.drawPath(path, strokePaint(Color.WHITE,4f))

        val dotPaint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }

        for (i in points.indices step 2) {
            canvas.drawCircle(points[i], points[i + 1], dotRadius, dotPaint)
        }
    }

    private fun getPolygonPoints(): FloatArray {
        val points = FloatArray(polygonPoints * 2)
        val angleStep = 2 * Math.PI / polygonPoints
        val radius = Math.min(parentWidth, parentHeight) / 4f

        for (i in 0 until polygonPoints) {
            val angle = i * angleStep
            points[i * 2] = (parentWidth / 2 + radius * Math.cos(angle)).toFloat()
            points[i * 2 + 1] = (parentHeight / 2 + radius * Math.sin(angle)).toFloat()
        }

        matrix.mapPoints(points)
        pointsArray = points//일단 이렇게 저장
        return points
    }

    fun touchInsideImage(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        val points = getPolygonPoints()
        val path = Path().apply {
            moveTo(points[0], points[1])
            for (i in 2 until points.size step 2) {
                lineTo(points[i], points[i + 1])
            }
            close()
        }

        val region = Region()
        val rectF = RectF()
        path.computeBounds(rectF, true)
        region.setPath(path, Region(rectF.left.toInt(), rectF.top.toInt(), rectF.right.toInt(), rectF.bottom.toInt()))

        return region.contains(x.toInt(), y.toInt())
    }


    override fun onTouch(v: View?, event: MotionEvent): Boolean {


        touchPoint(event)
        if(!touchInsideImage(event)){
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
                    var dx = event.x - lastTouchX
                    var dy = event.y - lastTouchY

                    matrix.postTranslate(dx, dy)
                    imageMatrix = matrix

                    lastTouchX = event.x
                    lastTouchY = event.y
                }
            }
        }

        invalidate()
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var scale = detector.scaleFactor
            val currentScaleFactor = scaleFactor * scale
            val maxScale = Math.min(parentWidth / drawable.intrinsicWidth.toFloat(),parentHeight / drawable.intrinsicHeight.toFloat())
            val minScale = 0.1f

            if (currentScaleFactor > maxScale) {
                scale = maxScale / scaleFactor
            } else if (currentScaleFactor < minScale) {
                scale = minScale / scaleFactor
            }

            scaleFactor *= scale
            matrix.postScale(scale, scale, detector.focusX, detector.focusY)
            imageMatrix = matrix
            invalidate()
            return true
        }
    }
}