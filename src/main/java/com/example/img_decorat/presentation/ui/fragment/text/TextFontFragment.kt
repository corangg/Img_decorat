package com.example.img_decorat.presentation.ui.fragment.text

import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentTextFontBinding
import com.example.img_decorat.presentation.ui.adapter.FontsAdapter
import com.example.img_decorat.presentation.ui.base.BaseFragment
import com.example.img_decorat.presentation.viewmodel.MainViewModel
import com.example.img_decorat.utils.ItemClickInterface
import com.example.img_decorat.utils.Util
import com.example.img_decorat.utils.UtilList

class TextFontFragment : BaseFragment<FragmentTextFontBinding, MainViewModel>(),
    ItemClickInterface {
    private lateinit var fontAdapter: FontsAdapter

    override fun layoutResId() = R.layout.fragment_text_font

    override fun getViewModelClass() = MainViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
        adapterSet()
    }

    override fun onItemClick(position: Int) {
        viewModel.textFontSet(position)
    }

    private fun adapterSet() {
        fontAdapter = FontsAdapter(UtilList.typefaces, this)
        Util.setLinearAdapter(binding.textFont, requireContext(), fontAdapter, 1)
    }
}