package com.example.img_decorat.presentation.ui.activity

import android.content.Intent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.img_decorat.R
import com.example.img_decorat.data.model.dataModels.LoadData
import com.example.img_decorat.databinding.ActivitySaveDataBinding
import com.example.img_decorat.presentation.ui.adapter.GridLoadViewAdapter
import com.example.img_decorat.presentation.ui.adapter.LinearLoadViewAdapter
import com.example.img_decorat.presentation.ui.base.BaseActivity
import com.example.img_decorat.presentation.viewmodel.SaveDataViewModel
import com.example.img_decorat.utils.ItemClickInterface
import com.example.img_decorat.utils.Util
import com.example.img_decorat.utils.Util.buttonColorToggle
import com.example.img_decorat.utils.Util.setLinearAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveDataActivity : BaseActivity<ActivitySaveDataBinding, SaveDataViewModel>(),
    ItemClickInterface {
    private lateinit var linearLoadViewAdapter: LinearLoadViewAdapter
    private lateinit var gridLoadViewAdapter: GridLoadViewAdapter

    override fun layoutResId() = R.layout.activity_save_data

    override fun getViewModelClass() = SaveDataViewModel::class.java

    override fun initializeUI() {
        binding.viewmodel = viewModel
        setSupportActionBar(binding.saveToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    override fun onLoadItemClick(position: Int, clickedItem: Int) {
        viewModel.clickedLoadItem(type = clickedItem, position = position)
    }

    override fun setObserve() {
        viewModel.recyclerLayoutManagerSet.observe(this) {
            viewModel.dataTitleList.value?.let { list ->
                buttonColorToggle(
                    binding.menuLinear,
                    getColor(R.color.pointcolor),
                    getColor(R.color.backgroundcolor),
                    it
                )
                buttonColorToggle(
                    binding.menuGride,
                    getColor(R.color.backgroundcolor),
                    getColor(R.color.pointcolor),
                    it
                )
                if (it) {
                    linearAdapterSet(list)
                } else {
                    gridAdapterSet(list)
                }
            }
        }

        viewModel.finisheLoadDataActivity.observe(this) {
            finish()
        }

        viewModel.showToastMessage.observe(this) {
            when (it) {
                0 -> toast(getString(R.string.saveToast))
            }
        }

        viewModel.openLoadData.observe(this) {
            if (it) {
                val title = viewModel.selectItemName
                setResultIntent(Intent().putExtra(getString(R.string.name), title))
            }
        }
    }

    private fun linearAdapterSet(list: List<LoadData>) {
        linearLoadViewAdapter = LinearLoadViewAdapter(list = list, onItemClickListener = this)
        setLinearAdapter(binding.recycleLoadData, this, linearLoadViewAdapter)
        binding.recycleLoadData.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    private fun gridAdapterSet(list: List<LoadData>) {
        gridLoadViewAdapter = GridLoadViewAdapter(list = list, onItemClickListener = this)
        Util.setGridAdapter(binding.recycleLoadData, this, 2, gridLoadViewAdapter)
    }
}