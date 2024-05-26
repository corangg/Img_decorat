package com.example.img_decorat.ui.animation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class LoadingAnimation @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF000000.toInt() // Black color
    }

    private val dotRadius = 20f
    private val dotSpacing = 100f
    private val animationDuration = 1200L

    private var scaleFactor = floatArrayOf(1f, 1f, 1f)

    init {
        startAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f

        for (i in 0 until 3) {
            val x = centerX + (i - 1) * dotSpacing
            canvas.drawCircle(x, centerY, dotRadius * scaleFactor[i], dotPaint)
        }
    }

    private fun startAnimation() {
        /*for (i in 0 until 3) {
            val animator = ValueAnimator.ofFloat(1f, 1.5f, 1f).apply {
                duration = animationDuration
                //startDelay = i * (animationDuration / 2)
                startDelay = i * animationDuration
                interpolator = LinearInterpolator()
                repeatCount = ValueAnimator.INFINITE
                addUpdateListener { animation ->
                    scaleFactor[i] = animation.animatedValue as Float
                    invalidate()
                }
            }
            animator.start()
        }*/
        val animator = ValueAnimator.ofFloat(1f, 1.5f, 1f).apply {
            duration = animationDuration
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animation ->
                val scale = animation.animatedValue as Float
                for (i in 0 until 3) {
                    scaleFactor[i] = scale
                }
                invalidate()
            }
        }
        animator.start()
    }
}