package com.example.img_decorat.presentation.ui.fragment.emoji

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentEmojiGroupBinding
import com.example.img_decorat.presentation.ui.adapter.EmojiGroupAdapter
import com.example.img_decorat.presentation.ui.base.BaseFragment
import com.example.img_decorat.presentation.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class EmojiGroupFragment : BaseFragment<FragmentEmojiGroupBinding, MainViewModel>() {
    override fun layoutResId() = R.layout.fragment_emoji_group

    override fun getViewModelClass() = MainViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
        adapterViewpager()
    }

    private fun adapterViewpager() {
        val emojiListSize = viewModel.emojiList.value?.size
        emojiListSize?.let {
            binding.viewpager.adapter = EmojiGroupAdapter(requireActivity(), emojiListSize)
            TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
                if (position < viewModel.emojiList.value!!.size) {
                    loadImageIntoTab(tab, viewModel.emojiList.value!![position].groupList[0])
                }
            }.attach()
        }
    }

    private fun loadImageIntoTab(tab: TabLayout.Tab, bitmap: Bitmap) {
        val imageView = ImageView(context)
        Glide.with(this)
            .load(bitmap)
            .override(64, 64)
            .into(imageView)

        tab.customView = imageView
    }
}