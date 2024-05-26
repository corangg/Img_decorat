package com.example.img_decorat.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.activity.viewModels
import com.example.img_decorat.R
import com.example.img_decorat.databinding.ActivityImageSplitBinding
import com.example.img_decorat.data.repository.LayerListRepository
import com.example.img_decorat.ui.base.BaseActivity
import com.example.img_decorat.ui.view.BTNAnimation
import com.example.img_decorat.ui.view.SplitCircleView
import com.example.img_decorat.ui.view.SplitPolygonView
import com.example.img_decorat.ui.view.SplitSquareView
import com.example.img_decorat.viewmodel.SplitViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ImageSplitActivity : BaseActivity<ActivityImageSplitBinding>() {
    private val viewmodel: SplitViewModel by viewModels()

    private lateinit var splitSquareView: SplitSquareView
    private lateinit var splitCircleView: SplitCircleView
    private lateinit var splitPolygonView: SplitPolygonView

    override fun layoutResId(): Int {
        return R.layout.activity_image_split
    }

    override fun initializeUI() {
        binding.viewmodel = viewmodel
        setIntent()
    }

    private fun setIntent(){
        val url = intent.getStringExtra("image")
        url?.let {
            viewmodel.intentToBitmap(url)
        }
    }

    override fun setObserve(){
        viewmodel.splitSquareView.observe(this){
            splitSquareView = it
            binding.frameSplitImage.addView(splitSquareView)
            splitSquareView.visibility = View.GONE
        }

        viewmodel.splitCircleView.observe(this){
            splitCircleView = it
            binding.frameSplitImage.addView(splitCircleView)
            splitCircleView.visibility = View.GONE
        }

        viewmodel.splitPolygonView.observe(this){
            splitPolygonView = it
            binding.frameSplitImage.addView(splitPolygonView)
            splitPolygonView.visibility = View.GONE
        }


        viewmodel.selectToolbar.observe(this){
            when(it){
                0->{
                    animation.buttionAnimation(binding.imgBtnBack)
                    finish()
                }
                1->{
                    animation.buttionAnimation(binding.imgBtnPrevious)
                }
                2->{
                    animation.buttionAnimation(binding.imgBtnNext)
                }
                3->{
                    animation.buttionAnimation(binding.imgBtnSplit)
                }
                4->{
                    animation.buttionAnimation(binding.imgBtnCheck)
                    setResultIntent()
                }
            }
        }

        viewmodel.selectSplitItem.observe(this){
            allSplitViewGone()
            when(it){
                0->{
                    splitSquareView.visibility = View.VISIBLE
                }
                1->{
                    splitCircleView.visibility = View.VISIBLE
                }
                2->{
                    binding.linearPolygon.visibility = View.VISIBLE
                    splitPolygonView.visibility = View.VISIBLE
                }
            }
        }
        viewmodel.currentImage.observe(this){
            binding.imgViewSplit.setImageBitmap(it)
        }

        viewmodel.polygonPoint.observe(this){
            splitPolygonView.setPolygon(it)
        }

        viewmodel.previousStackState.observe(this){
            if(it){
                binding.imgBtnPrevious.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            }else{
                binding.imgBtnPrevious.backgroundTintList = ColorStateList.valueOf(0xFF494949.toInt())
            }
        }
        viewmodel.nextStackState.observe(this){
            if(it){
                binding.imgBtnNext.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            }
            else{
                binding.imgBtnNext.backgroundTintList = ColorStateList.valueOf(0xFF494949.toInt())
            }
        }
    }

    private fun setResultIntent(){
        val resultIntent = Intent()
        resultIntent.putExtra("splitBitamp",viewmodel.splitImageUri.toString())
        setResult(RESULT_OK,resultIntent)
        finish()
    }

    private fun allSplitViewGone(){
        binding.linearPolygon.visibility = View.GONE
        splitSquareView.visibility = View.GONE
        splitPolygonView.visibility = View.GONE
        splitCircleView.visibility = View.GONE
    }
}