package com.example.img_decorat.ui.fragment.text

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentBackGroundBinding
import com.example.img_decorat.databinding.FragmentTextColorBinding
import com.example.img_decorat.ui.adapter.ColorAdapter
import com.example.img_decorat.utils.UtilList
import com.example.img_decorat.viewmodel.MainViewModel

class TextColorFragment : Fragment(),ColorAdapter.OnColorItemClickListener {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentTextColorBinding
    lateinit var textColorAdapter: ColorAdapter
    lateinit var backGroundColorAdapter: ColorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_text_color,container,false)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewModel

        textColorAdapterSet()
        backGroundColorAdapterSet()

        return binding.root
    }


    override fun onColorItemClick(position: Int, case: Int) {
        when(case){
            0->{
                viewModel.textColorSet(position)
            }
            1->{
                viewModel.textBackgroundColorSet(position)
            }
        }

    }

    private fun textColorAdapterSet(){
        binding.recycleTextColor.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        textColorAdapter = ColorAdapter(UtilList.colorsList, this,0)
        binding.recycleTextColor.adapter = textColorAdapter
    }

    private fun backGroundColorAdapterSet(){
        binding.recycleTextBackgroundColor.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        backGroundColorAdapter = ColorAdapter(UtilList.colorsList, this,1)
        binding.recycleTextBackgroundColor.adapter = backGroundColorAdapter
    }

}