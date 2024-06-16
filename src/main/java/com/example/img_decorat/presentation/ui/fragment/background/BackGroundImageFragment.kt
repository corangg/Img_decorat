package com.example.img_decorat.presentation.ui.fragment.background

import com.example.img_decorat.R
import com.example.img_decorat.data.model.dataModels.unsplashimagedata.UnsplashData
import com.example.img_decorat.databinding.FragmentBackGroundImageBinding
import com.example.img_decorat.presentation.ui.adapter.ImageAdapter
import com.example.img_decorat.presentation.ui.base.BaseFragment
import com.example.img_decorat.presentation.viewmodel.MainViewModel
import com.example.img_decorat.utils.ItemClickInterface
import com.example.img_decorat.utils.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackGroundImageFragment : BaseFragment<FragmentBackGroundImageBinding, MainViewModel>(),
    ItemClickInterface {
    private lateinit var imageAdapter: ImageAdapter

    override fun layoutResId() = R.layout.fragment_back_ground_image

    override fun getViewModelClass() = MainViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
        setObserve()
    }

    override fun onItemClick(position: Int) {
        viewModel.selectImage(position)
    }

    private fun setObserve() {
        viewModel.unsplashList.observe(viewLifecycleOwner) {
            adapterSet(it)
            Util.hideSoftKeyboard(requireActivity())
        }
    }

    private fun adapterSet(list: List<UnsplashData>) {
        imageAdapter = ImageAdapter(list, this)
        Util.setGridAdapter(binding.imageRecycle, requireContext(), 4, imageAdapter)
    }
}