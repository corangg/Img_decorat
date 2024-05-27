package com.example.img_decorat.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.img_decorat.R
import com.example.img_decorat.data.model.dataModels.LoadData
import com.example.img_decorat.databinding.ActivitySaveDataBinding
import com.example.img_decorat.ui.adapter.GridLoadViewAdapter
import com.example.img_decorat.ui.adapter.LinearLoadViewAdapter
import com.example.img_decorat.ui.base.BaseActivity
import com.example.img_decorat.viewmodel.SaveDataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveDataActivity : BaseActivity<ActivitySaveDataBinding>(),
    LinearLoadViewAdapter.OnLinearLoadItemClickListener,
    GridLoadViewAdapter.OnGridLoadItemClickListener{
    private val viewModel : SaveDataViewModel by viewModels()
    private lateinit var linearLoadViewAdapter: LinearLoadViewAdapter
    private lateinit var gridLoadViewAdapter: GridLoadViewAdapter


    override fun layoutResId(): Int {
        return R.layout.activity_save_data
    }

    override fun initializeUI() {
        binding.viewmodel = viewModel

        setSupportActionBar(binding.saveToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    override fun onLinearLoadItemClick(position: Int, clickedItem: Int) {
        viewModel. clickedLoadItem(type = clickedItem, position = position)
    }

    override fun onGridLoadItemClick(position: Int, clickedItem: Int) {
        viewModel. clickedLoadItem(type = clickedItem, position = position)
    }

    override fun setObserve() {
        viewModel.recyclerLayoutManagerSet.observe(this){
            viewModel.dataTitleList.value?.let {list->
                if(it){
                    binding.menuLinear.backgroundTintList = ColorStateList.valueOf(0xFFE3CDF2.toInt())
                    binding.menuGride.backgroundTintList = ColorStateList.valueOf(0xFFDFDFDF.toInt())
                    linearAdapterSet(list)
                }else{
                    binding.menuGride.backgroundTintList = ColorStateList.valueOf(0xFFE3CDF2.toInt())
                    binding.menuLinear.backgroundTintList = ColorStateList.valueOf(0xFFDFDFDF.toInt())
                    gridAdapterSet(list)
                }
            }
        }

        viewModel.finisheLoadDataActivity.observe(this){
            finish()
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

    private fun linearAdapterSet(list : List<LoadData>){
        binding.recycleLoadData.layoutManager = LinearLayoutManager(this)//GridLayoutManager(this, 2)
        linearLoadViewAdapter = LinearLoadViewAdapter(list = list, onItemClickListener = this)
        binding.recycleLoadData.adapter = linearLoadViewAdapter
        binding.recycleLoadData.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    private fun gridAdapterSet(list : List<LoadData>){
        binding.recycleLoadData.layoutManager = GridLayoutManager(this, 2)
        gridLoadViewAdapter = GridLoadViewAdapter(list = list, onItemClickListener = this)
        binding.recycleLoadData.adapter = gridLoadViewAdapter
    }

    private fun setResultIntent(){
        val title = viewModel.selectItemName
        val resultIntent = Intent()
        resultIntent.putExtra("name",title)
        setResult(RESULT_OK,resultIntent)
        finish()
    }
}