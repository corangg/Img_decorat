package com.example.img_decorat.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.img_decorat.R
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.databinding.ActivityMainBinding
import com.example.img_decorat.ui.adapter.LayerAdapter
import com.example.img_decorat.ui.adapter.MenuAdapter
import com.example.img_decorat.ui.base.BaseActivity
import com.example.img_decorat.ui.fragment.background.BackGroundFragment
import com.example.img_decorat.ui.fragment.emoji.EmojiGroupFragment
import com.example.img_decorat.ui.fragment.hueFragment.HueFragment
import com.example.img_decorat.ui.fragment.text.TextFragment
import com.example.img_decorat.ui.uihelper.MainActivityHelper
import com.example.img_decorat.utils.Util
import com.example.img_decorat.utils.UtilList
import com.example.img_decorat.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.LinkedList

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(),
    MenuAdapter.OnItemClickListener,
    LayerAdapter.OnLayerItemClickListener {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var menuAdapter: MenuAdapter
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
            viewModel.imgAddLayerList(it.data)
        }
    }

    val requestLoadData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            it.data?.let { intentdata ->
                val name = intentdata.getStringExtra("name")
                viewModel.loadData(name)
            }
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
        menuAdapterSet()
        itemTouchHelper()
    }

    override fun onItemClick(position: Int) {
        viewModel.selectToolbarMenu(position, binding.imgView)
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
        viewModel.openDrawerLayout.observe(this){
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }

        viewModel.openGalleryEvent.observe(this){
            animation.buttionAnimation(binding.menuAdd)
            openGalleryEvent()
        }

        viewModel.openMenuEvent.observe(this){
            uiHelper.openMenuEvent(it)
        }

        viewModel.openSaveDataActivity.observe(this){
            openSaveDataActivity()
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

        viewModel.startloading.observe(this){
            if(it){
                binding.loadingAnimation.visibility = View.VISIBLE
            }else{
                binding.loadingAnimation.visibility = View.GONE
            }
        }
        viewModel.showToast.observe(this){
            when(it){
                0->{
                    Toast.makeText(this,"선택된 이미지가 없습니다.", Toast.LENGTH_SHORT).show()
                }
                1->{
                    Toast.makeText(this,"저장 완료.",Toast.LENGTH_SHORT).show()
                }
                2->{
                    Toast.makeText(this,"이미지 저장 완료.",Toast.LENGTH_SHORT).show()
                }
            }
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
    }

    private fun menuAdapterSet(){///흐으음 맘에 너무 안드는데
        menuAdapter = MenuAdapter(UtilList.menuList, animation,this)
        binding.recycleMeun.adapter = menuAdapter
        binding.recycleMeun.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
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

    private fun openSaveDataActivity(){
        val intent = Intent(this, SaveDataActivity::class.java)
        requestLoadData.launch(intent)
    }

    private fun flameLayoutSet(list: LinkedList<ViewItemData>){
        binding.imgView.removeAllViews()
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