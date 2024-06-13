package com.example.img_decorat.presentation.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import com.example.img_decorat.presentation.ui.uihelper.ViewHelper
import com.example.img_decorat.viewmodel.MainViewModel
import kotlin.math.abs

class TextImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatEditText(context, attrs, defStyle), View.OnTouchListener {
    private val viewHelper = ViewHelper()
    private val matrix = Matrix()

    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private val rotateGestureDetector = RotateGestureDetector(RotateListener())

    var scaleFactor = 1.0f
    var rotationDegrees = 0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    var saturationValue = 1f
    var brightnessValue = 1f

    private var touchListener = true

    private var isEditable = false

    private var viewModel: MainViewModel? = null

    var fillBackgroundPaint = Paint().apply {
        color =Color.TRANSPARENT
        style =Paint.Style.FILL
    }

    init {
        setOnTouchListener(this)
        ViewCompat.setTranslationZ(this, 1f)
        gravity = Gravity.CENTER
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        super.dispatchTouchEvent(event)
        if(!touchListener){
            (parent as? ViewGroup)?.onTouchEvent(event)
        }
        return touchListener
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        touchListener = true
        val transPos = getTransformedPoints()

        if (!judgeTouchableArea(event.x, event.y, transPos)) {
            isEditable = false
            clearFocus()
            hideKeyboard()
            viewModel?.setViewText(this.text.toString())
            touchListener = false

            return false
        }

        scaleGestureDetector.onTouchEvent(event)
        rotateGestureDetector.onTouchEvent(event)

        if (!scaleGestureDetector.isInProgress && !rotateGestureDetector.isInProgress) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.x
                    lastTouchY = event.y
                    isEditable = true
                    viewModel?.selectLastImage(this.id)
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY

                    if(abs(dx)>1|| abs(dy)>1){
                        isEditable = false
                    }

                    matrix.postTranslate(dx, dy)
                    lastTouchX = event.x
                    lastTouchY = event.y
                }
                MotionEvent.ACTION_UP->{
                    if(isEditable){
                        showKeyboard()
                    }
                }
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        drawBorder(canvas)
        canvas.save()
        canvas.concat(matrix)
        super.onDraw(canvas)
        canvas.restore()
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

        if(viewModel?.lastTouchedImageId?.value == this.id){
            canvas.drawPath(path, viewHelper.borderPaint(Color.WHITE,4f))
            canvas.drawPath(path, fillBackgroundPaint)
        }else{
            canvas.drawPath(path, viewHelper.borderPaint(Color.TRANSPARENT))
            canvas.drawPath(path, fillBackgroundPaint)
        }
    }

    fun setBackgroundClolor(color: Int){
        fillBackgroundPaint.color = color
        invalidate()
    }

    fun setViewModel(viewModel: MainViewModel) {
        this.viewModel = viewModel
    }

    fun getTransformedPoints(): FloatArray {
        val width = this.width.toFloat()
        val height = this.height.toFloat()
        val points = floatArrayOf(
            0f, 0f,
            width, 0f,
            width, height,
            0f, height
        )
        matrix.mapPoints(points)
        return points
    }

    private fun judgeTouchableArea(x: Float, y: Float, polygon: FloatArray): Boolean {
        var intersectCount = 0
        for (i in polygon.indices step 2) {
            val x1 = polygon[i]
            val y1 = polygon[i + 1]
            val x2 = polygon[(i + 2) % polygon.size]
            val y2 = polygon[(i + 3) % polygon.size]

            if (((y1 > y) != (y2 > y)) &&
                (x < (x2 - x1) * (y - y1) / (y2 - y1) + x1)) {
                intersectCount++
            }
        }
        return (intersectCount % 2) == 1
    }

    private fun showKeyboard() {
        setFocusableInTouchMode(true)
        requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        setFocusableInTouchMode(false)
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun getMatrixValues(): FloatArray {
        val matrixValues = FloatArray(9)
        matrix.getValues(matrixValues)
        return matrixValues
    }

    fun setMatrixData(matrixValue: FloatArray, scale: Float, degrees: Float){
        matrix.setValues(matrixValue)
        scaleFactor = scale
        rotationDegrees = degrees

    }

    fun setTransparency(alpha: Float) {
        val clampedAlpha = Math.max(0f, Math.min(alpha, 100f)) / 100f
        this.alpha = clampedAlpha
        invalidate()
    }

    fun setSaturation(saturation: Float) {
        saturationValue = (saturation + 100f)/100f
        paint.colorFilter = viewHelper.applyColorFilter(saturationValue, brightnessValue)
    }

    fun setBrightness(brightness: Float) {
        brightnessValue = 0.008f*brightness +1f
        paint.colorFilter = viewHelper.applyColorFilter(saturationValue, brightnessValue)
    }

    fun getBackgroundColor(): Int {
        return fillBackgroundPaint.color
    }

    fun getTextColor(): Int {
        return currentTextColor
    }

    fun getTextTypeface(): Typeface {
        return typeface
    }
    fun getTextContent(): String {
        return text.toString()
    }

    fun getTextSizeValue(): Int{
        return (textSize/3).toInt()
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scale = detector.scaleFactor
            scaleFactor *= scale
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f))

            matrix.postScale(scale, scale, detector.focusX, detector.focusY)
            invalidate()
            isEditable = false
            return true
        }
    }

    private inner class RotateListener : RotateGestureDetector.OnRotateGestureListener {
        override fun onRotate(detector: RotateGestureDetector): Boolean {
            val rotation = detector.rotationDegreesDelta
            rotationDegrees += rotation
            matrix.postRotate(rotation, detector.focusX, detector.focusY)
            invalidate()
            isEditable = false
            return true
        }
    }
}