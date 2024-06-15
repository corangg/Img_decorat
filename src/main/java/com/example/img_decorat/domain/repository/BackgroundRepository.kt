package com.example.img_decorat.domain.repository

import android.widget.FrameLayout

interface BackgroundRepository {
    fun setBackgroundScale(item: Int, screenWidth: Int): FrameLayout.LayoutParams
}