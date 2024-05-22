package com.example.img_decorat.ui.uihelper

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.opengl.Visibility
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.databinding.ActivityMainBinding
import com.example.img_decorat.ui.activity.ImageSplitActivity
import com.example.img_decorat.ui.adapter.LayerAdapter
import com.example.img_decorat.ui.adapter.MenuAdapter
import com.example.img_decorat.ui.fragment.background.BackGroundFragment
import com.example.img_decorat.ui.fragment.emoji.EmojiGroupFragment
import com.example.img_decorat.ui.fragment.hueFragment.HueFragment
import com.example.img_decorat.ui.fragment.text.TextFragment
import com.example.img_decorat.utils.RequestCode
import com.example.img_decorat.utils.UtilList
import com.example.img_decorat.viewmodel.MainViewModel
import java.util.LinkedList

class MainActivityHelper(
    private val activity: AppCompatActivity,
    private val binding: ActivityMainBinding, ) {
    lateinit var menuAdapter: MenuAdapter
    lateinit var layerAdapter: LayerAdapter

    fun checkPermission(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RequestCode.OPEN_GALLERY_REQUEST_CODE)
        }
    }

    fun getScreenWith(): Int{
        val displayMetrics = activity.resources.displayMetrics
        return displayMetrics.widthPixels
    }

    fun recycleViewSet(){
        binding.recycleLayer.layoutManager = LinearLayoutManager(activity)
        binding.recycleMeun.layoutManager = LinearLayoutManager(activity)
    }

    fun layerAdapterSet(list : LinkedList<LayerItemData>){
        layerAdapter = LayerAdapter(list, activity as LayerAdapter.OnLayerItemClickListener)
        binding.recycleLayer.adapter = layerAdapter
    }

    fun menuAdapterSet(){///흐으음 맘에 너무 안드는데
        menuAdapter = MenuAdapter(UtilList.menuList,activity as MenuAdapter.OnItemClickListener)
        binding.recycleMeun.adapter = menuAdapter
        binding.recycleMeun.addItemDecoration(
            DividerItemDecoration(activity,LinearLayoutManager.VERTICAL)
        )
    }

    fun openMenuEvent(visibility: Boolean){
        if(visibility){
            binding.menuView.visibility = View.VISIBLE
        }else{
            binding.menuView.visibility = View.GONE
        }
    }

    fun setBackgroundImage(uri : String){
        Glide.with(binding.root).load(uri).into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                binding.imgView.background = resource
            }
            override fun onLoadCleared(placeholder: Drawable?) {}
        })
    }
}