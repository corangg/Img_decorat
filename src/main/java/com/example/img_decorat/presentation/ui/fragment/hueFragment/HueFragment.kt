package com.example.img_decorat.presentation.ui.fragment.hueFragment

import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentHueBinding
import com.example.img_decorat.presentation.ui.base.BaseFragment
import com.example.img_decorat.viewmodel.MainViewModel

class HueFragment : BaseFragment<FragmentHueBinding, MainViewModel>() {

    override fun layoutResId() = R.layout.fragment_hue

    override fun getViewModelClass() = MainViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
    }
}