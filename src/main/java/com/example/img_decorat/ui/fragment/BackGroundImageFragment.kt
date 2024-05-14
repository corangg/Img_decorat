package com.example.img_decorat.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.img_decorat.R
import com.example.img_decorat.Util
import com.example.img_decorat.dataModels.UnsplashData
import com.example.img_decorat.databinding.FragmentBackGroundColorBinding
import com.example.img_decorat.databinding.FragmentBackGroundImageBinding
import com.example.img_decorat.ui.activity.MainActivity
import com.example.img_decorat.ui.adapter.ColorAdapter
import com.example.img_decorat.ui.adapter.ImageAdapter
import com.example.img_decorat.viewmodel.MainViewModel

class BackGroundImageFragment : Fragment(),ImageAdapter.OnColorItemClickListener {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentBackGroundImageBinding
    lateinit var imageAdapter: ImageAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { binding = DataBindingUtil.inflate(inflater, R.layout.fragment_back_ground_image,container,false)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewModel

        setObserve()
        return binding.root
    }

    override fun onImageItemClick(position: Int) {
        viewModel.selectImage(position)
    }

    private fun adapterSet(list:MutableList<UnsplashData>){
        binding.imageRecycle.layoutManager = GridLayoutManager(requireContext(),4)//LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        imageAdapter = ImageAdapter(list, this)
        binding.imageRecycle.adapter = imageAdapter
    }
    private fun setObserve(){
        viewModel.unsplashList.observe(viewLifecycleOwner){
            adapterSet(it)
            hideKeyboard()
        }

    }

    private fun hideKeyboard() {
        val view = activity?.currentFocus
        view?.let {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

}