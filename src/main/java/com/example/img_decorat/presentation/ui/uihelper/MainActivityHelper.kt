package com.example.img_decorat.presentation.ui.uihelper

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.databinding.ActivityMainBinding
import com.example.img_decorat.presentation.ui.adapter.LayerAdapter
import com.example.img_decorat.presentation.ui.adapter.MenuAdapter
import com.example.img_decorat.utils.RequestCode
import com.example.img_decorat.presentation.viewmodel.MainViewModel
import java.util.LinkedList

class MainActivityHelper(
    private val activity: AppCompatActivity,
    private val binding: ActivityMainBinding,
    private val viewModel: MainViewModel
) {
    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                RequestCode.OPEN_GALLERY_REQUEST_CODE
            )
        }
    }

    fun loadingAnimaition(boolean: Boolean){
        if (boolean) {
            binding.loadingAnimation.visibility = View.VISIBLE
        } else {
            binding.loadingAnimation.visibility = View.GONE
        }
    }

    fun getScreenWith(): Int {
        val displayMetrics = activity.resources.displayMetrics
        return displayMetrics.widthPixels
    }

/*
    fun recycleViewSet() {
        binding.recycleLayer.layoutManager = LinearLayoutManager(activity)
        binding.recycleMeun.layoutManager = LinearLayoutManager(activity)
    }
*/

    fun openMenuEvent(visibility: Boolean) {
        if (visibility) {
            binding.menuView.visibility = View.VISIBLE
        } else {
            binding.menuView.visibility = View.GONE
        }
    }

    fun setBackgroundImage(uri: String) {
        Glide.with(binding.root).load(uri).into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                binding.imgView.background = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
    }

    fun flameLayoutSet(list: LinkedList<ViewItemData>) {
        binding.imgView.removeAllViews()
        for (i in list) {
            if (i.visible == true) {
                if (i.type == 0) {
                    i.img.setViewModel(viewModel)
                    binding.imgView.addView(i.img)
                } else if (i.type == 1) {
                    i.text.setViewModel(viewModel)
                    binding.imgView.addView(i.text)
                }
            }
        }
    }
}