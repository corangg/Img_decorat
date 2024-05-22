package com.example.img_decorat.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.img_decorat.ui.fragment.emoji.EmojiFragment

class EmojiGroupAdapter(activity: FragmentActivity, listSize : Int): FragmentStateAdapter(activity) {
    val size = listSize
    override fun getItemCount(): Int {
        return size
    }
    override fun createFragment(position: Int): Fragment {
        return EmojiFragment()
    }
}