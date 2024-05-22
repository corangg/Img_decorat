package com.example.img_decorat.utils

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.img_decorat.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("bind:viewPagerPageChange")
    fun bindViewPagerPageChange(viewPager: ViewPager2, viewModel: MainViewModel) {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.emojiTab.value = position
            }
        })
    }

    @JvmStatic
    @BindingAdapter("bind:tabLayoutPageChange")
    fun bindTabLayoutPageChange(tabLayout: TabLayout, viewModel: MainViewModel) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.emojiTab.value = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    @JvmStatic
    @BindingAdapter("onMenuItemClick")
    fun setOnMenuItemClickListener(toolbar: Toolbar, listener: Toolbar.OnMenuItemClickListener?) {
        toolbar.setOnMenuItemClickListener(listener)
    }
}