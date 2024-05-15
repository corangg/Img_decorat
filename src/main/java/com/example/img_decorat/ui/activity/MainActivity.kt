package com.example.img_decorat.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.img_decorat.dataModels.ImgLayerData
import com.example.img_decorat.utils.RequestCode
import com.example.img_decorat.databinding.ActivityMainBinding
import com.example.img_decorat.ui.adapter.LayerAdapter
import com.example.img_decorat.ui.adapter.MenuAdapter
import com.example.img_decorat.ui.fragment.BackGroundFragment
import com.example.img_decorat.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.LinkedList
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),MenuAdapter.OnItemClickListener,LayerAdapter.OnLayerItemClickListener {
    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var menuAdapter: MenuAdapter
    lateinit var layerAdapter: LayerAdapter

    lateinit var backGroundFragment: BackGroundFragment
    var startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK){
            true
        }
    }

    val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK && it.data != null){
            viewModel.setImgLayerList(it.data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, com.example.img_decorat.R.layout.activity_main)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewModel

        checkPermission()
        setToolbar()
        getScreenWith()
        menuAdapterSet()
        itemTouchHelper()
        setObserve()
    }


    private fun checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RequestCode.OPEN_GALLERY_REQUEST_CODE)
        }
    }

    private fun setToolbar(){
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        drawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, com.example.img_decorat.R.string.drawer_open,
            com.example.img_decorat.R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerToggle.syncState()
    }

    private fun getScreenWith(){
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        viewModel.screenWith = screenWidth
    }

    private fun menuAdapterSet(){
        val menuList : MutableList<String> = mutableListOf("열기", "저장", "출력")
        binding.recycleMeun.layoutManager = LinearLayoutManager(this)
        menuAdapter = MenuAdapter(menuList,this)
        binding.recycleMeun.adapter = menuAdapter
        binding.recycleMeun.addItemDecoration(
            DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        )
    }

    private fun layerAdapterSet(list : LinkedList<ImgLayerData>){//필요한것만 리셋해야할까?
        binding.recycleLayer.layoutManager = LinearLayoutManager(this)
        layerAdapter = LayerAdapter(list,this)
        binding.recycleLayer.adapter = layerAdapter
    }

    fun itemTouchHelper(){
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

    private fun setObserve(){
        viewModel.openGalleryEvent.observe(this){
            buttionAnimation(binding.menuAdd)
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            requestGalleryLauncher.launch(intent)
        }
        viewModel.openMenuEvent.observe(this){

            if(it){
                buttionAnimation(binding.menuMore)
                binding.menuView.visibility = View.VISIBLE
            }else{
                binding.menuView.visibility = View.GONE
            }
        }

        viewModel.liveLayerList.observe(this){
            layerAdapterSet(it)
        }

        viewModel.liveImageViewList.observe(this){
            binding.imgView.removeAllViews()//잠깐 쓰는거//새로 생성하면 비효율 적일거 같음
            for(i in it){
                if(i.visible == true){
                    i.img.setViewModel(viewModel)
                    binding.imgView.addView(i.img)
                }
            }
        }

        viewModel.selectNavigationItem.observe(this){
            when(it){
                0->{
                    backGroundFragment = BackGroundFragment()
                    supportFragmentManager.beginTransaction().replace(binding.detailNavigaionView.id,backGroundFragment).commit()
                }
                1->{

                    val image = viewModel.lastTouchedImage
                    val intent = Intent(this, ImageSplitActivity::class.java)
                    intent.putExtra("image",image)
                    //startActivity(intent)
                    startForResult.launch(intent)
                }
            }
        }

        viewModel.backGroundColor.observe(this){
            binding.imgView.setBackgroundColor(it)
        }



        viewModel.selectBackgroundScale.observe(this){
            binding.imgView.layoutParams = it
        }

        viewModel.selectBackGroundImage.observe(this){
            if(it != null){
                Glide.with(binding.root).load(it).into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        binding.imgView.background = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })

            }
        }

        viewModel.lastTouchedImageId.observe(this){
            binding.imgView.invalidate()

        }
    }

    private fun buttionAnimation(button: ImageButton) {
        val button = button
        val animation = AnimationUtils.loadAnimation(this, com.example.img_decorat.R.anim.anim_clicked)
        button.startAnimation(animation)
    }

}