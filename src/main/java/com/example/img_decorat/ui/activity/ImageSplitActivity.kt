package com.example.img_decorat.ui.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.img_decorat.R
import com.example.img_decorat.databinding.ActivityImageSplitBinding
import com.example.img_decorat.repository.LayerListRepository
import com.example.img_decorat.ui.view.BTNAnimation
import com.example.img_decorat.ui.view.SplitPolygonView
import com.example.img_decorat.ui.view.SplitSquareVIew
import com.example.img_decorat.viewmodel.SplitViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImageSplitActivity : AppCompatActivity() {
    lateinit var binding : ActivityImageSplitBinding
    private val viewmodel: SplitViewModel by viewModels()

    private lateinit var splitSquareView: SplitSquareVIew
    private lateinit var splitPolygonView: SplitPolygonView


    @Inject
    lateinit var animation: BTNAnimation
    @Inject
    lateinit var layerListRepository: LayerListRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_image_split)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewmodel

        setObserve()
        setIntent()
        setSplitView()

    }
    private fun setIntent(){
        val url = intent.getStringExtra("image")
        if(url != null){
            viewmodel.intentToBitmap(url)
        }
    }

    private fun setSplitView(){
        if(viewmodel.splitSquareView.value != null){
            splitSquareView = viewmodel.splitSquareView.value!!
            binding.splitImgView.addView(splitSquareView)
            splitSquareView.visibility = View.GONE
        }
        if(viewmodel.splitPolygonView.value != null){
            splitPolygonView = viewmodel.splitPolygonView.value!!
            binding.splitImgView.addView(splitPolygonView)
            splitPolygonView.visibility = View.GONE
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
                    animation.buttionAnimation(binding.splitBtn)
                }
                4->{
                    animation.buttionAnimation(binding.checkBtn)
                }
            }
        }
        viewmodel.selectSplitItem.observe(this){
            when(it){
                0->{
                    binding.sliderPolygon.visibility = View.GONE
                    splitPolygonView.visibility = View.GONE

                    if(splitSquareView.visibility == View.GONE){
                        splitSquareView.visibility = View.VISIBLE
                    }
                }
                1->{
                    binding.sliderPolygon.visibility = View.VISIBLE
                    splitSquareView.visibility = View.GONE

                    if(splitPolygonView.visibility == View.GONE){
                        splitPolygonView.visibility = View.VISIBLE
                    }

                }
                2->{
                    binding.sliderPolygon.visibility = View.GONE
                }
            }
        }
        viewmodel.splitImage.observe(this){
            binding.splitImg.setImageBitmap(it)
        }

        viewmodel.polygonPoint.observe(this){
            splitPolygonView.setPolygone(it)
        }

        viewmodel.undoStackBoolean.observe(this){
            if(it){
                binding.undoBtn.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            }else{
                binding.undoBtn.backgroundTintList = ColorStateList.valueOf(0xFF494949.toInt())
            }
        }
        viewmodel.runStackBoolean.observe(this){
            if(it){
                binding.runBtn.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            }
            else{
                binding.runBtn.backgroundTintList = ColorStateList.valueOf(0xFF494949.toInt())
            }
        }
    }


}