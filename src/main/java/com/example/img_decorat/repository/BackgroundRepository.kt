package com.example.img_decorat.repository

import android.widget.FrameLayout

class BackgroundRepository {
    fun setBackgroundScale(item:Int, screenWith : Int): FrameLayout.LayoutParams{
        var layoutParams = FrameLayout.LayoutParams(0,0)
        when(item){
            0->{
                layoutParams  = FrameLayout.LayoutParams(screenWith, screenWith)
            }
            1->{
                val heightPx = screenWith * 0.75
                layoutParams = FrameLayout.LayoutParams(screenWith, heightPx.toInt())
            }
            2->{
                val withPx = screenWith * 0.75
                layoutParams = FrameLayout.LayoutParams(withPx.toInt(),screenWith)
            }
            3->{
                val heightPx = screenWith * 0.66
                layoutParams = FrameLayout.LayoutParams(screenWith, heightPx.toInt())
            }
            4->{
                val withPx = screenWith * 0.66
                layoutParams = FrameLayout.LayoutParams(withPx.toInt(),screenWith)
            }
            5->{
                val heightPx = screenWith * 0.5625
                layoutParams = FrameLayout.LayoutParams(screenWith, heightPx.toInt())
            }
            6->{
                val withPx = screenWith * 0.5625
                layoutParams = FrameLayout.LayoutParams(withPx.toInt(),screenWith)
            }
        }
        return layoutParams
    }
}