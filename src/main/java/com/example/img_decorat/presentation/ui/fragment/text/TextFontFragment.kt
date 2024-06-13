package com.example.img_decorat.presentation.ui.fragment.text

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentTextFontBinding
import com.example.img_decorat.presentation.ui.adapter.FontsAdapter
import com.example.img_decorat.presentation.ui.base.BaseFragment
import com.example.img_decorat.utils.ItemClickInterface
import com.example.img_decorat.utils.Util
import com.example.img_decorat.utils.UtilList
import com.example.img_decorat.viewmodel.MainViewModel

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