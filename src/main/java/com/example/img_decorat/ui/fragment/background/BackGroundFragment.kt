package com.example.img_decorat.ui.fragment.background

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentBackGroundBinding
import com.example.img_decorat.ui.base.BaseFragment
import com.example.img_decorat.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackGroundFragment : BaseFragment<FragmentBackGroundBinding>() {
    private val viewModel: MainViewModel by activityViewModels()

    override fun layoutResId(): Int {
        return R.layout.fragment_back_ground
    }

    override fun initializeUI() {
        binding.viewmodel = viewModel
    }

    override fun setObserve(){
        viewModel.selectbackgroundMenu.observe(viewLifecycleOwner){
            when(it){
                0 -> parentFragmentManager.beginTransaction().replace(binding.backgroundItemView.id, BackGroundScaleFragment()).commit()
                1 -> parentFragmentManager.beginTransaction().replace(binding.backgroundItemView.id, BackGroundColorFragment()).commit()
                2 -> parentFragmentManager.beginTransaction().replace(binding.backgroundItemView.id, BackGroundImageFragment()).commit()
            }
        }
    }
}