package com.example.img_decorat.ui.fragment.emoji

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentEmojiBinding
import com.example.img_decorat.ui.adapter.EmojiAdapter
import com.example.img_decorat.ui.base.BaseFragment
import com.example.img_decorat.viewmodel.MainViewModel

class EmojiFragment :
    BaseFragment<FragmentEmojiBinding>(),
    EmojiAdapter.OnEmojiItemClickListener {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var emojiAdapter: EmojiAdapter

    override fun layoutResId(): Int {
        return R.layout.fragment_emoji
    }

    override fun initializeUI() {
        binding.viewmodel = viewModel
    }

    override fun onEmojiItemClick(position: Int) {
        viewModel.addEmogeLayer(position)
    }

    override fun setObserve(){
        viewModel.emojiTab.observe(viewLifecycleOwner){
            adapterSet()
        }
    }

    private fun adapterSet(){
        val tabIndex = viewModel.emojiTab.value
        val list = viewModel.emojiList.value
        tabIndex?.let {
            list?.let {
                val emojiList = list[tabIndex].groupList
                binding.emojiRecycle.layoutManager = GridLayoutManager(requireContext(),8)
                emojiAdapter = EmojiAdapter(emojiList,this)
                binding.emojiRecycle.adapter = emojiAdapter
            }
        }
    }
}