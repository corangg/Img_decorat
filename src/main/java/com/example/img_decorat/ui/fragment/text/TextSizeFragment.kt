package com.example.img_decorat.ui.fragment.text

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentTextColorBinding
import com.example.img_decorat.databinding.FragmentTextFontBinding
import com.example.img_decorat.databinding.FragmentTextSizeBinding
import com.example.img_decorat.ui.base.BaseFragment
import com.example.img_decorat.viewmodel.MainViewModel

class TextSizeFragment : BaseFragment<FragmentTextSizeBinding>() {
    private val viewModel: MainViewModel by activityViewModels()

    override fun layoutResId(): Int {
        return R.layout.fragment_text_size
    }

    override fun initializeUI() {
        binding.viewmodel = viewModel
    }

    override fun setObserve() {}
}