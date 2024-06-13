package com.example.img_decorat.presentation.ui.uihelper

import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

class ViewHelper {


    fun borderPaint(colorValue: Int, stroke: Float = 0f): Paint {
        return Paint().apply {
            color = colorValue
            style = Paint.Style.STROKE
            strokeWidth = stroke

        }
    }

    fun applyColorFilter(saturationValue: Float, brightnessValue:Float) :ColorFilter{
        val colorMatrix = ColorMatrix()

        val saturationMatrix = ColorMatrix()
        saturationMatrix.setSaturation(saturationValue)

        val brightnessMatrix = ColorMatrix()
        brightnessMatrix.setScale(brightnessValue, brightnessValue, brightnessValue, 1f)

        colorMatrix.postConcat(saturationMatrix)
        colorMatrix.postConcat(brightnessMatrix)

        return ColorMatrixColorFilter(colorMatrix)
    }


}