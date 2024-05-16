package com.example.img_decorat.ui.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.img_decorat.R
import com.example.img_decorat.databinding.ActivityImageSplitBinding
import com.example.img_decorat.ui.view.BTNAnimation
import com.example.img_decorat.ui.view.SplitAreaView
import com.example.img_decorat.viewmodel.SplitViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImageSplitActivity : AppCompatActivity() {
    lateinit var binding : ActivityImageSplitBinding
    private val viewmodel: SplitViewModel by viewModels()

    @Inject
    lateinit var animation: BTNAnimation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_image_split)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewmodel

        setIntent()
        setObserve()
    }
    private fun setIntent(){
        val url = intent.getStringExtra("image")
        if(url != null){
            Glide.with(binding.root).load(url).into(binding.splitImg)
        }

    }

    private fun setObserve(){
        viewmodel.selectToolbar.observe(this){
            when(it){
                0->{
                    animation.buttionAnimation(binding.backBtn)
                    finish()
                }
                1->{
                    animation.buttionAnimation(binding.undoBtn)

                }
                2->{
                    animation.buttionAnimation(binding.runBtn)
                }
                3->{
                    animation.buttionAnimation(binding.checkBtn)
                }
            }
        }
        viewmodel.selectSplitItem.observe(this){
            when(it){
                0->{
                    val splitArea = SplitAreaView(this).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        )
                        setImageBitmap(createTransparentBitmap(512,512))
                    }

                    binding.splitImgView.addView(splitArea)
                }
            }
        }
    }

    fun createTransparentBitmap(width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.TRANSPARENT)
        }
    }



}