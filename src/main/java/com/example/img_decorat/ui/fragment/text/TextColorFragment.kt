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
import com.example.img_decorat.databinding.FragmentBackGroundBinding
import com.example.img_decorat.databinding.FragmentTextColorBinding
import com.example.img_decorat.ui.adapter.ColorAdapter
import com.example.img_decorat.ui.base.BaseFragment
import com.example.img_decorat.utils.UtilList
import com.example.img_decorat.viewmodel.MainViewModel

class TextColorFragment : BaseFragment<FragmentTextColorBinding>(), ColorAdapter.OnColorItemClickListener {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var textColorAdapter: ColorAdapter
    private lateinit var backGroundColorAdapter: ColorAdapter

    override fun layoutResId(): Int {
        return R.layout.fragment_text_color
    }

    override fun initializeUI() {
        binding.viewmodel = viewModel
        textColorAdapterSet()
        backGroundColorAdapterSet()
    }

    override fun onColorItemClick(position: Int, case: Int) {
        when(case){
            0 -> viewModel.textColorSet(position)
            1 -> viewModel.textBackgroundColorSet(position)
        }
    }

    override fun setObserve() {}

    private fun textColorAdapterSet(){
        binding.recycleTextColor.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        textColorAdapter = ColorAdapter(UtilList.colorsList, this,0)
        binding.recycleTextColor.adapter = textColorAdapter
    }

    private fun backGroundColorAdapterSet(){
        binding.recycleTextBackgroundColor.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        backGroundColorAdapter = ColorAdapter(UtilList.colorsList, this,1)
        binding.recycleTextBackgroundColor.adapter = backGroundColorAdapter
    }
}