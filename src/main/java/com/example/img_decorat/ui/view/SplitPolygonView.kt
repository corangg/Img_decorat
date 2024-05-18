package com.example.img_decorat.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide.init

class SplitPolygonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, type: Int
) : AppCompatImageView(context, attrs, defStyle), View.OnTouchListener {

    private val matrix = Matrix()
    var polygonPoints: Int = 3
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

    fun getParentSize():Pair<Int,Int>{
        return Pair(parentWidth,parentHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        parentWidth = (parent as View).width
        parentHeight = (parent as View).height
        updatePolygonPoints()
    }

    fun setPolygon(polygon: Int) {
        polygonPoints = polygon
        updatePolygonPoints()
    }

    private fun updatePolygonPoints() {
        pointsArray = getPolygonPoints()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)
    }

    fun strokePaint(strokeColor: Int, thickness: Float): Paint {
        return Paint().apply {
            color = strokeColor
            style = Paint.Style.STROKE
            strokeWidth = thickness
        }
    }

    private fun drawBorder(canvas: Canvas) {
        val points = pointsArray
        val path = Path().apply {
            moveTo(points[0], points[1])
            for (i in 2 until points.size step 2) {
                lineTo(points[i], points[i + 1])
            }
            close()
        }
        canvas.drawPath(path, strokePaint(Color.WHITE, 4f))

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
        return points
    }

    private fun touchPoint(event: MotionEvent) {
        val x = event.x
        val y = event.y

        for (i in pointsArray.indices step 2) {
            if (x in pointsArray[i] - 50..pointsArray[i] + 50 && y in pointsArray[i + 1] - 50..pointsArray[i + 1] + 50) {
                movingPointIndex = i
                return
            }
        }
        movingPointIndex = -1
    }

    private fun updatePointPosition(event: MotionEvent) {
        if (movingPointIndex != -1) {
            pointsArray[movingPointIndex] = event.x
            pointsArray[movingPointIndex + 1] = event.y
            invalidate()
        }
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                lastTouchY = event.y
                touchPoint(event)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                if (movingPointIndex != -1) {
                    updatePointPosition(event)
                } else if (touchInsideImage(event)) {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY

                    for (i in pointsArray.indices step 2) {
                        pointsArray[i] += dx
                        pointsArray[i + 1] += dy
                    }

                    lastTouchX = event.x
                    lastTouchY = event.y
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                movingPointIndex = -1
            }
        }

        return true
    }

    fun touchInsideImage(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        val path = Path().apply {
            moveTo(pointsArray[0], pointsArray[1])
            for (i in 2 until pointsArray.size step 2) {
                lineTo(pointsArray[i], pointsArray[i + 1])
            }
            close()
        }

        val region = Region()
        val rectF = RectF()
        path.computeBounds(rectF, true)

        region.setPath(path, Region(rectF.left.toInt(), rectF.top.toInt(), rectF.right.toInt(), rectF.bottom.toInt()))

        return region.contains(x.toInt(), y.toInt())
    }

    fun getPolygonPath(): Path {
        return Path().apply {
            moveTo(pointsArray[0], pointsArray[1])
            for (i in 2 until pointsArray.size step 2) {
                lineTo(pointsArray[i], pointsArray[i + 1])
            }
            close()
        }
    }
}