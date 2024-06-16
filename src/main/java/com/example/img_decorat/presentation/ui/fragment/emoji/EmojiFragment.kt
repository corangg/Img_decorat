package com.example.img_decorat.presentation.ui.fragment.emoji

import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentEmojiBinding
import com.example.img_decorat.presentation.ui.adapter.EmojiAdapter
import com.example.img_decorat.presentation.ui.base.BaseFragment
import com.example.img_decorat.presentation.viewmodel.MainViewModel
import com.example.img_decorat.utils.ItemClickInterface
import com.example.img_decorat.utils.Util

class EmojiFragment :
    BaseFragment<FragmentEmojiBinding, MainViewModel>(),
    ItemClickInterface {
    private lateinit var emojiAdapter: EmojiAdapter

    override fun layoutResId() = R.layout.fragment_emoji

    override fun getViewModelClass() = MainViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
        setObserve()
    }

    override fun onItemClick(position: Int) {
        viewModel.addEmogeLayer(position)
    }

    private fun setObserve() {
        viewModel.emojiTab.observe(viewLifecycleOwner) {
            adapterSet()
        }
    }

    private fun adapterSet() {
        val tabIndex = viewModel.emojiTab.value
        val list = viewModel.emojiList.value

        tabIndex?.let { tab ->
            list?.let { list ->
                val emojiList = list[tab].groupList
                emojiAdapter = EmojiAdapter(emojiList, this)
                Util.setGridAdapter(binding.emojiRecycle, requireContext(), 8, emojiAdapter)
            }
        }
    }
}