package com.example.img_decorat.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.img_decorat.R
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.utils.RequestCode
import com.example.img_decorat.databinding.ActivityMainBinding
import com.example.img_decorat.ui.adapter.LayerAdapter
import com.example.img_decorat.ui.adapter.MenuAdapter
import com.example.img_decorat.ui.base.BaseActivity
import com.example.img_decorat.ui.fragment.background.BackGroundFragment
import com.example.img_decorat.ui.fragment.emoji.EmojiGroupFragment
import com.example.img_decorat.ui.fragment.hueFragment.HueFragment
import com.example.img_decorat.ui.fragment.text.TextFragment
import com.example.img_decorat.ui.uihelper.MainActivityHelper
import com.example.img_decorat.ui.view.BTNAnimation
import com.example.img_decorat.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.LinkedList
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(),
    MenuAdapter.OnItemClickListener,
    LayerAdapter.OnLayerItemClickListener {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var layerAdapter: LayerAdapter
    private lateinit var uiHelper: MainActivityHelper


    var startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK){
            val url =it.data?.getStringExtra("splitBitamp")?.toUri()
            if(url!=null){
                viewModel.reseultSplitView(url)
            }
        }
    }

    val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK && it.data != null){
            viewModel.setImgLayerList(it.data)
        }
    }

    override fun layoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initializeUI() {
        binding.viewmodel = viewModel
        uiHelper = MainActivityHelper(this, binding)

        uiHelper.checkPermission()
        setToolbar()
        viewModel.setViewSize(uiHelper.getScreenWith())
        uiHelper.recycleViewSet()
        uiHelper.menuAdapterSet()
        itemTouchHelper()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(position: Int) {
        position
    }

    override fun onLayerItemClick(position: Int) {
        viewModel.selectLayer(position)
    }

    override fun onCheckedClick(position: Int, checked: Boolean) {
        viewModel.updateChecked(position, checked)
    }

    override fun onLayerDelete(position: Int) {
        viewModel.deleteLayer(position)
    }

    override fun setObserve(){
        viewModel.openGalleryEvent.observe(this){
            animation.buttionAnimation(binding.menuAdd)
            openGalleryEvent()
        }

        viewModel.openMenuEvent.observe(this){
            uiHelper.openMenuEvent(it)
        }

        viewModel.liveLayerList.observe(this){
            layerAdapterSet(it)
        }

        viewModel.liveViewList.observe(this){
            flameLayoutSet(it)
        }

        viewModel.selectNavigationItem.observe(this){
            if(it !=1 && binding.detailNavigaionView.visibility == View.GONE){
                binding.detailNavigaionView.visibility = View.VISIBLE
            }
            when(it){
                0->supportFragmentManager.beginTransaction().replace(binding.detailNavigaionView.id, BackGroundFragment()).commit()
                1->{
                    binding.detailNavigaionView.visibility = View.GONE
                    val image = viewModel.lastTouchedImage
                    openSplitActivity(image)
                }
                2->supportFragmentManager.beginTransaction().replace(binding.detailNavigaionView.id, HueFragment()).commit()
                3->supportFragmentManager.beginTransaction().replace(binding.detailNavigaionView.id, EmojiGroupFragment()).commit()
                4->supportFragmentManager.beginTransaction().replace(binding.detailNavigaionView.id, TextFragment()).commit()
            }
        }

        viewModel.backGroundColor.observe(this){
            binding.imgView.setBackgroundColor(it)
        }

        viewModel.selectBackgroundScale.observe(this){
            binding.imgView.layoutParams = it
        }

        viewModel.selectBackGroundImage.observe(this){
            it?.let {
                uiHelper.setBackgroundImage(it)
            }
        }

        viewModel.lastTouchedImageId.observe(this){
            binding.imgView.invalidate()

        }
    }

    private fun itemTouchHelper(){
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                layerAdapter.moveItem(fromPos, toPos)
                viewModel.swapImageView(fromPos,toPos)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recycleLayer)
    }

    private fun setToolbar(){
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        drawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, com.example.img_decorat.R.string.drawer_open,
            com.example.img_decorat.R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerToggle.syncState()
    }

    private fun layerAdapterSet(list : LinkedList<LayerItemData>){
        layerAdapter = LayerAdapter(list,this)
        binding.recycleLayer.adapter = layerAdapter
    }

    private fun openGalleryEvent(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        requestGalleryLauncher.launch(intent)
    }

    private fun openSplitActivity(uri: Uri){
        val intent = Intent(this, ImageSplitActivity::class.java)
        intent.putExtra("image",uri.toString())
        startForResult.launch(intent)
    }

    private fun flameLayoutSet(list: LinkedList<ViewItemData>){
        binding.imgView.removeAllViews()//잠깐 쓰는거//새로 생성하면 비효율 적일거 같음
        for(i in list){
            if(i.visible == true){
                if(i.type == 0){
                    i.img.setViewModel(viewModel)
                    binding.imgView.addView(i.img)
                }else if(i.type ==1){
                    i.text.setViewModel(viewModel)
                    binding.imgView.addView(i.text)
                }
            }
        }
    }

}