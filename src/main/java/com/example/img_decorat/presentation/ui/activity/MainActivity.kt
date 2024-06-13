package com.example.img_decorat.presentation.ui.activity

import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import androidx.core.net.toUri
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.img_decorat.R
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.databinding.ActivityMainBinding
import com.example.img_decorat.presentation.ui.adapter.LayerAdapter
import com.example.img_decorat.presentation.ui.adapter.MenuAdapter
import com.example.img_decorat.presentation.ui.base.BaseActivity
import com.example.img_decorat.presentation.ui.fragment.background.BackGroundFragment
import com.example.img_decorat.presentation.ui.fragment.emoji.EmojiGroupFragment
import com.example.img_decorat.presentation.ui.fragment.hueFragment.HueFragment
import com.example.img_decorat.presentation.ui.fragment.text.TextFragment
import com.example.img_decorat.presentation.ui.uihelper.MainActivityHelper
import com.example.img_decorat.utils.ItemClickInterface
import com.example.img_decorat.utils.Util.setLinearAdapter
import com.example.img_decorat.utils.UtilList
import com.example.img_decorat.utils.toastMessageUtil
import com.example.img_decorat.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.LinkedList

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(),
    ItemClickInterface {
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var layerAdapter: LayerAdapter
    private lateinit var uiHelper: MainActivityHelper


    val startForResult = registerForActivityResultHandler { result ->
        val url = result.data?.getStringExtra(getString(R.string.splitBitmap))?.toUri()
        url?.let {
            viewModel.reseultSplitView(it)
        }
    }

    val requestGalleryLauncher = registerForActivityResultHandler { result ->
        result.data?.let {
            viewModel.imgAddLayerList(it)
        }
    }

    val requestLoadData = registerForActivityResultHandler { result ->
        result.data?.let {
            val name = it.getStringExtra(getString(R.string.name))
            viewModel.loadData(name)
        }
    }

    override fun layoutResId(): Int = R.layout.activity_main

    override fun getViewModelClass() = MainViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
        uiHelper = MainActivityHelper(this, binding, viewModel)

        uiHelper.checkPermission()
        setToolbar(binding.mainToolbar)
        viewModel.setViewSize(uiHelper.getScreenWith())
        menuAdapterSet()
        itemTouchHelper()
    }

    override fun onMenuItemClick(position: Int) {
        viewModel.selectToolbarMenu(position, binding.imgView)
    }

    override fun onItemClick(position: Int) {
        viewModel.selectLayer(position)
    }

    override fun onCheckedClick(position: Int, checked: Boolean) {
        viewModel.updateChecked(position, checked)
    }

    override fun onDeleteItemClick(position: Int) {
        viewModel.deleteLayer(position)
    }

    override fun setObserve() {
        viewModel.openDrawerLayout.observe(this) {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }

        viewModel.openGalleryEvent.observe(this) {
            animation.buttionAnimation(binding.menuAdd)
            openGalleryEvent()
        }

        viewModel.openMenuEvent.observe(this) {
            uiHelper.openMenuEvent(it)
        }

        viewModel.openSaveDataActivity.observe(this) {
            openSaveDataActivity()
        }

        viewModel.liveLayerList.observe(this) {
            layerAdapterSet(it)
        }

        viewModel.liveViewList.observe(this) {
            uiHelper.flameLayoutSet(it)
        }

        viewModel.selectNavigationItem.observe(this) {
            val fragmentArray =
                arrayOf(BackGroundFragment(), HueFragment(), EmojiGroupFragment(), TextFragment())
            if (it != 4) {
                if (binding.detailNavigaionView.visibility == View.GONE)
                    binding.detailNavigaionView.visibility = View.VISIBLE

                replaceFragment(binding.detailNavigaionView.id, fragmentArray[it])
            } else {
                binding.detailNavigaionView.visibility = View.GONE
                val image = viewModel.lastTouchedImage
                openSplitActivity(image)
            }
        }

        viewModel.backGroundColor.observe(this) {
            binding.imgView.setBackgroundColor(it)
        }

        viewModel.selectBackgroundScale.observe(this) {
            binding.imgView.layoutParams = it
        }

        viewModel.selectBackGroundImage.observe(this) {
            uiHelper.setBackgroundImage(it)
        }

        viewModel.lastTouchedImageId.observe(this) {
            binding.imgView.invalidate()
        }

        viewModel.startloading.observe(this) {
            uiHelper.loadingAnimaition(it)
        }

        viewModel.showToast.observe(this) {
            toast(toastMessageUtil.mainToast[it])
        }
    }

    private fun itemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                layerAdapter.moveItem(fromPos, toPos)
                viewModel.swapImageView(fromPos, toPos)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recycleLayer)
    }

    private fun menuAdapterSet() {
        menuAdapter = MenuAdapter(UtilList.menuList, this)
        setLinearAdapter(binding.recycleMeun, this, menuAdapter)
        binding.recycleMeun.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    private fun layerAdapterSet(list: LinkedList<LayerItemData>) {
        layerAdapter = LayerAdapter(list, this)
        setLinearAdapter(binding.recycleLayer, this, layerAdapter)
    }

    private fun openGalleryEvent() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = getString(R.string.intentImageType)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        requestGalleryLauncher.launch(intent)
    }

    private fun openSplitActivity(uri: Uri) {
        val intent = Intent(this, ImageSplitActivity::class.java)
        intent.putExtra(getString(R.string.image), uri.toString())
        startForResult.launch(intent)
    }

    private fun openSaveDataActivity() {
        val intent = Intent(this, SaveDataActivity::class.java)
        requestLoadData.launch(intent)
    }
}