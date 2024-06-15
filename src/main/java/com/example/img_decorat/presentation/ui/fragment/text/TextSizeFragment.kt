package com.example.img_decorat.presentation.ui.fragment.text

import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentTextSizeBinding
import com.example.img_decorat.presentation.ui.base.BaseFragment
import com.example.img_decorat.presentation.viewmodel.MainViewModel

class TextSizeFragment : BaseFragment<FragmentTextSizeBinding, MainViewModel>() {
    override fun layoutResId() = R.layout.fragment_text_size

    override fun getViewModelClass() = MainViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
    }
}