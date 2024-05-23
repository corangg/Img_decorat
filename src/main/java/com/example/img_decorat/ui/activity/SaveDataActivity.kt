package com.example.img_decorat.ui.activity

import android.view.Menu
import androidx.activity.viewModels
import com.example.img_decorat.R
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
    lateinit var editableImageView: EditableImageView

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

    override fun onLoadItemClick(position: Int) {
        true
    }

    override fun setObserve() {
        /*viewModel.getSaveDataList.observe(this){
            //adapterSet(it)
            //Util.deserializeView(it[0].viewData,binding.test,this)
            //testFrame(it[0].viewData)
        }*/
    }


    /*fun testFrame(viewDataString: String){
        val viewData = Gson().fromJson<Map<String, Any>>(viewDataString, object : TypeToken<Map<String, Any>>() {}.type)
        val viewType = viewData["type"] as String
        if(viewType == "FrameLayout"){
            val children = viewData["children"] as List<Map<String, Any>>
            for (i in children){
                val data = deserializeEditableImageView(Gson().toJson(i))
                when(data["type"] as String){
                    "EditableImageView" ->{
                        val editableImageView = Util.createEditableImageView(
                            context = this,

                        )
                    }


                }
            }

        }
    }*/

    fun deserializeEditableImageView(viewDataString: String): Map<String,Any> {
        val viewData = Gson().fromJson<Map<String, Any>>(viewDataString, object : TypeToken<Map<String, Any>>() {}.type)
        return viewData
    }

    /*private fun adapterSet(list : List<EditViewData>){
        binding.recycleLoadData.layoutManager = GridLayoutManager(this, 2)
        loadViewAdapter = LoadViewAdapter(this, list,this)
        binding.recycleLoadData.adapter = loadViewAdapter
    }*/
}