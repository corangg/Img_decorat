package com.example.img_decorat.utils

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.example.img_decorat.R

object FontsList {
    lateinit var typefaces: List<Typeface>

    fun initialize(context: Context) {
        typefaces = listOf(

            ResourcesCompat.getFont(context, R.font.font_hsyeoleum),
            ResourcesCompat.getFont(context, R.font.font_movesans),
            ResourcesCompat.getFont(context, R.font.font_freesentation),
            ResourcesCompat.getFont(context, R.font.font_onruyruy),
            ResourcesCompat.getFont(context, R.font.font_sweetreview)
        ).filterNotNull()
    }
}