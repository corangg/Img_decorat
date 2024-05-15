package com.example.img_decorat.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.img_decorat.R
import com.example.img_decorat.utils.Util
import com.example.img_decorat.databinding.FragmentBackGroundColorBinding
import com.example.img_decorat.ui.adapter.ColorAdapter
import com.example.img_decorat.utils.ColorList
import com.example.img_decorat.viewmodel.MainViewModel

class BackGroundColorFragment : Fragment(),ColorAdapter.OnColorItemClickListener {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentBackGroundColorBinding
    lateinit var colorAdapter: ColorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_back_ground_color,container,false)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewModel

        adapterSet()
        setObserve()
        return binding.root
    }

    override fun onColorItemClick(position: Int) {
        viewModel.selectBackgroundColor(position)
    }

    private fun adapterSet(){

        binding.colorRecyclerview.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        colorAdapter = ColorAdapter(ColorList.colorsList, this)
        binding.colorRecyclerview.adapter = colorAdapter
    }
    private fun setObserve(){

    }

}