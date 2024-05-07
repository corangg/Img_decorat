package com.example.img_decorat.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.LinearLayout
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.img_decorat.R
import com.example.img_decorat.RequestCode
import com.example.img_decorat.databinding.ActivityMainBinding
import com.example.img_decorat.ui.adapter.MenuAdapter
import com.example.img_decorat.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(),MenuAdapter.OnItemClickListener {
    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var menuAdapter: MenuAdapter

    private val imagesList = mutableListOf<Uri>()

    val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK && it.data != null){
            val data = it.data

            data?.clipData?.let{ clipData ->
                for (i in 0 until clipData.itemCount) {
                    val imageUri: Uri = clipData.getItemAt(i).uri
                    imagesList.add(imageUri)
                }
            } ?: data?.data?.let { uri ->
                imagesList.add(uri)
            }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(position: Int) {
        position
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
    }

    private fun buttionAnimation(button: ImageButton) {
        val button = button
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_clicked)
        button.startAnimation(animation)
    }

}