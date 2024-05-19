package com.example.img_decorat.utils

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.img_decorat.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout

object BindingAdapter {

    @JvmStatic
    @androidx.databinding.BindingAdapter("bind:viewPagerPageChange")
    fun bindViewPagerPageChange(viewPager: ViewPager2, viewModel: MainViewModel) {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setPageIndex(position)
            }
        })
    }

    @JvmStatic
    @BindingAdapter("bind:tabLayoutPageChange")
    fun bindTabLayoutPageChange(tabLayout: TabLayout, viewModel: MainViewModel) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.setPageIndex(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}