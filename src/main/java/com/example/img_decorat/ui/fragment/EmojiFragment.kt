package com.example.img_decorat.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.img_decorat.R
import com.example.img_decorat.dataModels.UnsplashData
import com.example.img_decorat.databinding.FragmentBackGroundImageBinding
import com.example.img_decorat.databinding.FragmentEmojiBinding
import com.example.img_decorat.ui.adapter.EmojiAdapter
import com.example.img_decorat.ui.adapter.ImageAdapter
import com.example.img_decorat.viewmodel.MainViewModel

class EmojiFragment : Fragment(),EmojiAdapter.OnEmojiItemClickListener {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentEmojiBinding
    lateinit var emojiAdapter: EmojiAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emoji,container,false)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewModel

        setObserve()
        return binding.root
    }

    override fun onEmojiItemClick(position: Int) {
        viewModel.addEmogeLayer(position)
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
    private fun setObserve(){
        viewModel.emojiTab.observe(viewLifecycleOwner){
            adapterSet()
        }
    }
}