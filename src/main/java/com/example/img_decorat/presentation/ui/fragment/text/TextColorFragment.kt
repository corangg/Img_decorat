package com.example.img_decorat.presentation.ui.fragment.text

import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentTextColorBinding
import com.example.img_decorat.presentation.ui.adapter.ColorAdapter
import com.example.img_decorat.presentation.ui.base.BaseFragment
import com.example.img_decorat.presentation.viewmodel.MainViewModel
import com.example.img_decorat.utils.ItemClickInterface
import com.example.img_decorat.utils.Util.setLinearAdapter
import com.example.img_decorat.utils.UtilList

class TextColorFragment : BaseFragment<FragmentTextColorBinding, MainViewModel>(),
    ItemClickInterface {
    private lateinit var textColorAdapter: ColorAdapter
    private lateinit var backGroundColorAdapter: ColorAdapter

    override fun layoutResId() = R.layout.fragment_text_color

    override fun getViewModelClass() = MainViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
        textColorAdapterSet()
        backGroundColorAdapterSet()
    }

    override fun onColorItemClick(position: Int, case: Int) {
        when (case) {
            0 -> viewModel.textColorSet(position)
            1 -> viewModel.textBackgroundColorSet(position)
        }
    }

    private fun textColorAdapterSet() {
        textColorAdapter = ColorAdapter(UtilList.colorsList, this, 0)
        setLinearAdapter(binding.recycleTextColor, requireContext(), textColorAdapter, 1)
    }

    private fun backGroundColorAdapterSet() {
        backGroundColorAdapter = ColorAdapter(UtilList.colorsList, this, 1)
        setLinearAdapter(
            binding.recycleTextBackgroundColor,
            requireContext(),
            backGroundColorAdapter,
            1
        )
    }
}