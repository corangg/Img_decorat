package com.example.img_decorat.presentation.ui.fragment.background

import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentBackGroundScaleBinding
import com.example.img_decorat.presentation.ui.base.BaseFragment
import com.example.img_decorat.viewmodel.MainViewModel

class BackGroundScaleFragment : BaseFragment<FragmentBackGroundScaleBinding, MainViewModel>() {
    private lateinit var selectScaleItem: LinearLayout

    override fun layoutResId() = R.layout.fragment_back_ground_scale

    override fun getViewModelClass() = MainViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
        setObserve()
        selectScaleItem = binding.backgroundScale
    }

    private fun setObserve() {
        viewModel.selectBackgroundItem.observe(viewLifecycleOwner) {
            val scaleArray = arrayOf(
                binding.scale11,
                binding.scale43,
                binding.scale34,
                binding.scale32,
                binding.scale23,
                binding.scale169,
                binding.scale916
            )

            selectScaleItem.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.pointcolor
                )
            )
            scaleArray[it].setBackgroundResource(R.drawable.bg_selectitem)
            selectScaleItem = scaleArray[it]
        }
    }
}