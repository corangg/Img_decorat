package com.example.img_decorat.presentation.ui.fragment.background

import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentBackGroundBinding
import com.example.img_decorat.presentation.ui.base.BaseFragment
import com.example.img_decorat.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackGroundFragment : BaseFragment<FragmentBackGroundBinding, MainViewModel>() {

    override fun layoutResId() = R.layout.fragment_back_ground

    override fun getViewModelClass() = MainViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
        setObserve()
    }

    private fun setObserve() {
        viewModel.selectbackgroundMenu.observe(viewLifecycleOwner) {
            val fragmentArray = arrayOf(
                BackGroundScaleFragment(),
                BackGroundColorFragment(),
                BackGroundImageFragment()
            )
            replaceFragment(binding.backgroundItemView.id, fragmentArray[it])
        }
    }
}