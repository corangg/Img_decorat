package com.example.img_decorat.ui.fragment.background

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.img_decorat.R
import com.example.img_decorat.data.model.dataModels.unsplashimagedata.UnsplashData
import com.example.img_decorat.databinding.FragmentBackGroundImageBinding
import com.example.img_decorat.ui.adapter.ImageAdapter
import com.example.img_decorat.ui.base.BaseFragment
import com.example.img_decorat.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackGroundImageFragment : BaseFragment<FragmentBackGroundImageBinding>(),ImageAdapter.OnImageItemClickListener {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var imageAdapter: ImageAdapter

    override fun layoutResId(): Int {
        return R.layout.fragment_back_ground_image
    }

    override fun initializeUI() {
        binding.viewmodel = viewModel
    }

    override fun onImageItemClick(position: Int) {
        viewModel.selectImage(position)
    }

    override fun setObserve(){
        viewModel.unsplashList.observe(viewLifecycleOwner){
            adapterSet(it)
            hideKeyboard()
        }
    }

    private fun adapterSet(list:MutableList<UnsplashData>){
        binding.imageRecycle.layoutManager = GridLayoutManager(requireContext(),4)//LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        imageAdapter = ImageAdapter(list, this)
        binding.imageRecycle.adapter = imageAdapter
    }

    private fun hideKeyboard() {
        val view = activity?.currentFocus
        view?.let {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}