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
import com.example.img_decorat.viewmodel.MainViewModel

class EditableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle), View.OnTouchListener {

    private val matrix = Matrix()
    private var scaleFactor = 1.0f
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private var lastTouchX = 0f
    private var lastTouchY = 0f
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

    fun setViewModel(viewModel: MainViewModel) {
        this.viewModel = viewModel
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {

        val pos = getCurrentImagePosition()
        val viewSize = getCurrentImageSize()

        val touchInsideImage = event.x >= pos.first && event.x <= pos.first + viewSize.first &&
                event.y >= pos.second && event.y <= pos.second + viewSize.second

        if (!touchInsideImage) {
            return false
        }

        scaleGestureDetector.onTouchEvent(event)

        if (!scaleGestureDetector.isInProgress) {
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
                }
            }
        }

        invalidate()
        return true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)
        invalidate()
    }

    private fun drawBorder(canvas: Canvas) {
        val pos = getCurrentImagePosition()
        val viewSize = getCurrentImageSize()
        val borderRect = RectF(
            pos.first,
            pos.second,
            pos.first + viewSize.first,
            pos.second + viewSize.second
        )
        if (viewModel?.lastTouchedImageId?.value == this.id) {
            canvas.drawRect(borderRect, selectBorderPaint)
        }else{
            canvas.drawRect(borderRect, unSelectBorderPaint)
        }
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
}
/*
class ZoomableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle), View.OnTouchListener {

    private val matrix = Matrix()
    private var scaleFactor = 1.0f
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private var lastTouchX = 0f  // 이전 터치의 X 좌표
    private var lastTouchY = 0f  // 이전 터치의 Y 좌표
    private val borderPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 4f  // 기본 테두리 두께
    }
    //private var isTouched = false
    //private var viewModel: MainViewModel? = null
    lateinit var viewModel: MainViewModel

    init {
        setOnTouchListener(this)
        scaleType = ScaleType.MATRIX
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 여기서 context를 ViewModelStoreOwner로 캐스팅하여 viewModel 초기화
        if (context is ViewModelStoreOwner) {
            viewModel = ViewModelProvider(context as ViewModelStoreOwner).get(MainViewModel::class.java)
        }
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pivotX = w / 2f
        pivotY = h / 2f
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val pos = getCurrentImagePosition()
        val viewSize = getCurrentImageSize()

        scaleGestureDetector.onTouchEvent(event)

        if (!scaleGestureDetector.isInProgress) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.x
                    lastTouchY = event.y
                    viewModel.lastTouchImageView.value = this
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY

                    matrix.postTranslate(dx, dy)
                    imageMatrix = matrix

                    lastTouchX = event.x
                    lastTouchY = event.y
                }
            }
        }

        val touch = event.x >= pos.first && event.x <= pos.first + viewSize.first && event.y >= pos.second && event.y <= pos.second + viewSize.second
        return touch
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (viewModel.lastTouchImageView.value == this) {
            drawBorder(canvas)
        }
    }

    private fun drawBorder(canvas: Canvas) {
        val pos = getCurrentImagePosition()
        val viewSize = getCurrentImageSize()


        val bolderRect = RectF(
            pos.first,
            pos.second,
            pos.first + viewSize.first,
            pos.second + viewSize.second
        )

        canvas.drawRect(bolderRect, borderPaint)
    }

    fun getCurrentImageSize(): Pair<Float, Float> {//현재 크기
        val values = FloatArray(9)
        matrix.getValues(values)
        val scaleX = values[Matrix.MSCALE_X]
        val scaleY = values[Matrix.MSCALE_Y]

        val drawable = drawable ?: return Pair(0f, 0f)
        val originalWidth = drawable.intrinsicWidth
        val originalHeight = drawable.intrinsicHeight

        return Pair(originalWidth * scaleX, originalHeight * scaleY)
    }

    fun getCurrentImagePosition(): Pair<Float, Float> {//현재 위치
        val values = FloatArray(9)
        matrix.getValues(values)
        val transX = values[Matrix.MTRANS_X]
        val transY = values[Matrix.MTRANS_Y]
        return Pair(transX, transY)
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
}*/
