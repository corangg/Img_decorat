package com.example.img_decorat.presentation.ui.fragment.background

import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentBackGroundColorBinding
import com.example.img_decorat.presentation.ui.adapter.ColorAdapter
import com.example.img_decorat.presentation.ui.base.BaseFragment
import com.example.img_decorat.utils.ItemClickInterface
import com.example.img_decorat.utils.Util.setLinearAdapter
import com.example.img_decorat.utils.UtilList
import com.example.img_decorat.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackGroundColorFragment : BaseFragment<FragmentBackGroundColorBinding, MainViewModel>(),
    ItemClickInterface {
    private lateinit var colorAdapter: ColorAdapter

    override fun layoutResId() = R.layout.fragment_back_ground_color

    override fun getViewModelClass() = MainViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
        adapterSet()
    }

    override fun onColorItemClick(position: Int, case: Int) {
        viewModel.selectBackgroundColor(UtilList.colorsList[position])
    }

    private fun adapterSet() {
        colorAdapter = ColorAdapter(UtilList.colorsList, this, 0)
        setLinearAdapter(binding.colorRecyclerview, requireContext(), colorAdapter, 1)
    }

}