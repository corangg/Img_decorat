package com.example.img_decorat.ui.fragment.emoji

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentEmojiGroupBinding
import com.example.img_decorat.ui.adapter.EmojiGroupAdapter
import com.example.img_decorat.ui.base.BaseFragment
import com.example.img_decorat.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class EmojiGroupFragment : BaseFragment<FragmentEmojiGroupBinding>() {
    private val viewModel: MainViewModel by activityViewModels()

    override fun layoutResId(): Int {
        return R.layout.fragment_emoji_group
    }

    override fun initializeUI() {
        binding.viewmodel = viewModel
        adapterViewpager()
    }

    override fun setObserve(){}

    private fun adapterViewpager(){
        val emojiListSize = viewModel.emojiList.value?.size
        emojiListSize?.let {
            binding.viewpager.adapter = EmojiGroupAdapter(requireActivity(),emojiListSize)
            TabLayoutMediator(binding.tabs,binding.viewpager){ tab,position->
                if(position<viewModel.emojiList.value!!.size){
                    loadImageIntoTab(tab,viewModel.emojiList.value!![position].groupList[0])
                }
            }.attach()
        }
    }

    private fun loadImageIntoTab(tab: TabLayout.Tab, bitmap: Bitmap) {
        val imageView = ImageView(context)
        Glide.with(this)
            .load(bitmap)
            .override(64,64)
            .into(imageView)

        tab.customView = imageView
    }
}