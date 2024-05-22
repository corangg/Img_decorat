package com.example.img_decorat.ui.fragment.text

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentTextFontBinding
import com.example.img_decorat.ui.adapter.FontsAdapter
import com.example.img_decorat.ui.base.BaseFragment
import com.example.img_decorat.utils.UtilList
import com.example.img_decorat.viewmodel.MainViewModel

class TextFontFragment : BaseFragment<FragmentTextFontBinding>(),FontsAdapter.OnFontItemClickListener {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var fontAdapter: FontsAdapter

    override fun layoutResId(): Int {
        return R.layout.fragment_text_font
    }

    override fun initializeUI() {
        binding.viewmodel = viewModel
        adapterSet()
    }

    override fun onFontItemClick(position: Int) {
        viewModel.textFontSet(position)
    }

    override fun setObserve(){}

    private fun adapterSet(){
        binding.textFont.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        fontAdapter = FontsAdapter(UtilList.typefaces, this)
        binding.textFont.adapter = fontAdapter
    }
}