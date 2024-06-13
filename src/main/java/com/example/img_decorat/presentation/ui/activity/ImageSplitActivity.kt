package com.example.img_decorat.presentation.ui.activity

import android.content.Intent
import android.view.View
import com.example.img_decorat.R
import com.example.img_decorat.databinding.ActivityImageSplitBinding
import com.example.img_decorat.presentation.ui.base.BaseActivity
import com.example.img_decorat.presentation.ui.view.SplitCircleView
import com.example.img_decorat.presentation.ui.view.SplitPolygonView
import com.example.img_decorat.presentation.ui.view.SplitSquareView
import com.example.img_decorat.utils.Util.buttonColorToggle
import com.example.img_decorat.viewmodel.SplitViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageSplitActivity : BaseActivity<ActivityImageSplitBinding, SplitViewModel>() {

    private lateinit var splitSquareView: SplitSquareView
    private lateinit var splitCircleView: SplitCircleView
    private lateinit var splitPolygonView: SplitPolygonView

    override fun layoutResId() = R.layout.activity_image_split

    override fun getViewModelClass() = SplitViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
        setToolbar(binding.toolbarSplit)
        setIntent()
    }

    override fun setObserve() {
        viewModel.splitSquareView.observe(this) {
            splitSquareView = it
            binding.frameSplitImage.addView(splitSquareView)

        }

        viewModel.splitCircleView.observe(this) {
            splitCircleView = it
            binding.frameSplitImage.addView(splitCircleView)

        }

        viewModel.splitPolygonView.observe(this) {
            splitPolygonView = it
            binding.frameSplitImage.addView(splitPolygonView)
        }

        viewModel.selectToolbar.observe(this) {
            val btnArray = arrayOf(
                binding.imgBtnBack,
                binding.imgBtnPrevious,
                binding.imgBtnNext,
                binding.imgBtnSplit,
                binding.imgBtnCheck
            )
            animation.buttionAnimation(btnArray[it])

            when (it) {
                0 -> finish()
                4 -> setResultIntent(
                    Intent().putExtra(
                        getString(R.string.splitBitmap),
                        viewModel.splitImageUri.toString()
                    )
                )
            }
        }

        viewModel.selectSplitItem.observe(this) {
            allSplitViewGone()
            when (it) {
                0 -> {
                    splitSquareView.visibility = View.VISIBLE
                }

                1 -> {
                    splitCircleView.visibility = View.VISIBLE
                }

                2 -> {
                    binding.linearPolygon.visibility = View.VISIBLE
                    splitPolygonView.visibility = View.VISIBLE
                }
            }
        }
        viewModel.currentImage.observe(this) {
            binding.imgViewSplit.setImageBitmap(it)
        }

        viewModel.polygonPoint.observe(this) {
            splitPolygonView.setPolygon(it)
        }

        viewModel.previousStackState.observe(this) {
            buttonColorToggle(
                binding.imgBtnPrevious,
                getColor(R.color.pointcolor),
                getColor(R.color.backgroundcolor),
                it
            )
        }
        viewModel.nextStackState.observe(this) {
            buttonColorToggle(
                binding.imgBtnNext,
                getColor(R.color.pointcolor),
                getColor(R.color.backgroundcolor),
                it
            )
        }
    }

    private fun setIntent() {
        val url = intent.getStringExtra(getString(R.string.image))
        url?.let {
            viewModel.intentToBitmap(it)
        }
    }

    private fun allSplitViewGone() {
        binding.linearPolygon.visibility = View.GONE
        splitSquareView.visibility = View.GONE
        splitPolygonView.visibility = View.GONE
        splitCircleView.visibility = View.GONE
    }
}