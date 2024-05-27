package com.example.img_decorat.ui.view

import android.content.Context
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BTNAnimation @Inject constructor(
    @ApplicationContext private val context: Context){
    fun buttionAnimation(button: ImageButton) {
        val button = button
        val animation = AnimationUtils.loadAnimation(context, com.example.img_decorat.R.anim.anim_clicked)
        button.startAnimation(animation)
    }

    fun textAnimaation(textView: TextView){
        val button = textView
        val animation = AnimationUtils.loadAnimation(context, com.example.img_decorat.R.anim.anim_clicked)
        button.startAnimation(animation)
    }

}