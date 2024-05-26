package com.example.img_decorat.ui.activity

import android.content.Intent
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.img_decorat.R
import com.example.img_decorat.data.model.dataModels.LoadData
import com.example.img_decorat.databinding.ActivitySaveDataBinding
import com.example.img_decorat.ui.adapter.LoadViewAdapter
import com.example.img_decorat.ui.base.BaseActivity
import com.example.img_decorat.ui.view.EditableImageView
import com.example.img_decorat.viewmodel.SaveDataViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveDataActivity : BaseActivity<ActivitySaveDataBinding>(),LoadViewAdapter.OnLoadItemClickListener {
    private val viewModel : SaveDataViewModel by viewModels()
    private lateinit var loadViewAdapter: LoadViewAdapter


    override fun layoutResId(): Int {
        return R.layout.activity_save_data
    }

    override fun initializeUI() {
        binding.viewmodel = viewModel

        setSupportActionBar(binding.saveToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_savedata,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onLoadItemClick(position: Int, clickedItem: Int) {
        viewModel. clickedLoadItem(type = clickedItem, position = position)
    }

    override fun setObserve() {
        viewModel.dataTitleList.observe(this){
            adapterSet(it)
        }
        viewModel.showToastMessage.observe(this){
            when(it){
                0->{
                    Toast.makeText(this,"선택된 이미지가 없습니다.",Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.openLoadData.observe(this){
            if(it){
                setResultIntent()
            }
        }
    }

    private fun adapterSet(list : List<LoadData>){
        binding.recycleLoadData.layoutManager = GridLayoutManager(this, 2)
        loadViewAdapter = LoadViewAdapter(list,this)
        binding.recycleLoadData.adapter = loadViewAdapter
    }

    private fun setResultIntent(){
        val title = viewModel.selectItemName
        val resultIntent = Intent()
        resultIntent.putExtra("name",title)
        setResult(RESULT_OK,resultIntent)
        finish()
    }
}