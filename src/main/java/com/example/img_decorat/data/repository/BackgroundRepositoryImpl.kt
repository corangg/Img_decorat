package com.example.img_decorat.data.repository

import android.widget.FrameLayout
import com.example.img_decorat.domain.repository.BackgroundRepository

class BackgroundRepositoryImpl : BackgroundRepository {
    override fun setBackgroundScale(item:Int, screenWidth : Int): FrameLayout.LayoutParams{
        var layoutParams = FrameLayout.LayoutParams(0,0)
        when(item){
            0->{
                layoutParams  = FrameLayout.LayoutParams(screenWidth, screenWidth)
            }
            1->{
                val heightPx = screenWidth * 0.75
                layoutParams = FrameLayout.LayoutParams(screenWidth, heightPx.toInt())
            }
            2->{
                val withPx = screenWidth * 0.75
                layoutParams = FrameLayout.LayoutParams(withPx.toInt(),screenWidth)
            }
            3->{
                val heightPx = screenWidth * 0.66
                layoutParams = FrameLayout.LayoutParams(screenWidth, heightPx.toInt())
            }
            4->{
                val withPx = screenWidth * 0.66
                layoutParams = FrameLayout.LayoutParams(withPx.toInt(),screenWidth)
            }
            5->{
                val heightPx = screenWidth * 0.5625
                layoutParams = FrameLayout.LayoutParams(screenWidth, heightPx.toInt())
            }
            6->{
                val withPx = screenWidth * 0.5625
                layoutParams = FrameLayout.LayoutParams(withPx.toInt(),screenWidth)
            }
        }
        return layoutParams
    }


}