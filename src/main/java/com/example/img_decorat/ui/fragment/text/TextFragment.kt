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
import com.example.img_decorat.databinding.FragmentTextBinding
import com.example.img_decorat.ui.base.BaseFragment
import com.example.img_decorat.viewmodel.MainViewModel

class TextFragment : BaseFragment<FragmentTextBinding>() {
    private val viewModel: MainViewModel by activityViewModels()

    override fun layoutResId(): Int {
        return R.layout.fragment_text
    }

    override fun initializeUI() {
        binding.viewmodel = viewModel
    }

    override fun setObserve(){
        viewModel.selectTextMenu.observe(viewLifecycleOwner){
            when(it){
                0 -> parentFragmentManager.beginTransaction().replace(binding.textItemView.id, TextFontFragment()).commit()
                1 -> parentFragmentManager.beginTransaction().replace(binding.textItemView.id, TextColorFragment()).commit()
                2 -> parentFragmentManager.beginTransaction().replace(binding.textItemView.id, TextSizeFragment()).commit()
            }
        }
    }
}