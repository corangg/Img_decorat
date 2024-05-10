package com.example.img_decorat.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.img_decorat.ImgLayerData
import com.example.img_decorat.R
import com.example.img_decorat.RequestCode
import com.example.img_decorat.databinding.ActivityMainBinding
import com.example.img_decorat.ui.adapter.LayerAdapter
import com.example.img_decorat.ui.adapter.MenuAdapter
import com.example.img_decorat.viewmodel.MainViewModel
import java.util.LinkedList

class MainActivity : AppCompatActivity(),MenuAdapter.OnItemClickListener,LayerAdapter.OnLayerItemClickListener {
    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var menuAdapter: MenuAdapter
    lateinit var layerAdapter: LayerAdapter

    val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK && it.data != null){
            viewModel.setImgLayerList(it.data)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewModel

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkPermission()
        setToolbar()
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
        drawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.drawer_open,R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerToggle.syncState()
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

    private fun layerAdapterSet(list : LinkedList<ImgLayerData>){
        binding.recycleLayer.layoutManager = LinearLayoutManager(this)
        layerAdapter = LayerAdapter(list,this)
        binding.recycleLayer.adapter = layerAdapter
    }

    private fun layerSet(list : LinkedList<ImgLayerData>){
        for(i in  list){
            if(i.check){
                val imageView = ImageView(this).apply {
                    id = i.id
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                    setImageBitmap(i.bitMap)
                }
                binding.imgView.addView(imageView)
            }
        }
    }

    fun itemTouchHelper(){
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

        viewModel.layerList.observe(this){
            layerAdapterSet(it)
            layerSet(it)
        }
    }

    private fun buttionAnimation(button: ImageButton) {
        val button = button
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_clicked)
        button.startAnimation(animation)
    }

}