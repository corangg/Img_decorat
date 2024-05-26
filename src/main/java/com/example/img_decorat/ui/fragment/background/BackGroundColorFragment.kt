package com.example.img_decorat.ui.fragment.background

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
import com.example.img_decorat.utils.Util
import com.example.img_decorat.databinding.FragmentBackGroundColorBinding
import com.example.img_decorat.ui.adapter.ColorAdapter
import com.example.img_decorat.ui.base.BaseFragment
import com.example.img_decorat.utils.UtilList
import com.example.img_decorat.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackGroundColorFragment : BaseFragment<FragmentBackGroundColorBinding>(),ColorAdapter.OnColorItemClickListener {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var colorAdapter : ColorAdapter

    override fun layoutResId(): Int {
        return R.layout.fragment_back_ground_color
    }

    override fun initializeUI() {
        binding.viewmodel = viewModel
        adapterSet()
    }

    override fun onColorItemClick(position: Int, case: Int) {
        viewModel.selectBackgroundColor(UtilList.colorsList[position])
    }

    override fun setObserve() {}

    private fun adapterSet(){
        binding.colorRecyclerview.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        colorAdapter = ColorAdapter(UtilList.colorsList, this,0)
        binding.colorRecyclerview.adapter = colorAdapter
    }

}