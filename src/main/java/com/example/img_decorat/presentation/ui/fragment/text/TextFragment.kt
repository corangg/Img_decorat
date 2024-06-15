package com.example.img_decorat.presentation.ui.fragment.text

import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentTextBinding
import com.example.img_decorat.presentation.ui.base.BaseFragment
import com.example.img_decorat.utils.Util.hideSoftKeyboard
import com.example.img_decorat.presentation.viewmodel.MainViewModel

class TextFragment : BaseFragment<FragmentTextBinding, MainViewModel>() {
    override fun layoutResId() = R.layout.fragment_text

    override fun getViewModelClass() = MainViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
        setObserve()
    }

    private fun setObserve() {
        viewModel.selectTextMenu.observe(viewLifecycleOwner) {
            val fragmentArray = arrayOf(TextFontFragment(), TextColorFragment(), TextSizeFragment())
            replaceFragment(binding.textItemView.id, fragmentArray[it])
            hideSoftKeyboard(requireActivity())
        }
    }
}