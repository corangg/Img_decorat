package com.example.img_decorat.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.img_decorat.presentation.ui.fragment.emoji.EmojiFragment

class EmojiGroupAdapter(activity: FragmentActivity, listSize: Int) :
    FragmentStateAdapter(activity) {
    val size = listSize
    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        return EmojiFragment()
    }
}