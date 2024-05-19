package com.example.img_decorat.ui.fragment.background

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentBackGroundBinding
import com.example.img_decorat.databinding.FragmentBackGroundScaleBinding
import com.example.img_decorat.viewmodel.MainViewModel


class BackGroundScaleFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentBackGroundScaleBinding

    lateinit var selectScaleItem : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_back_ground_scale,container,false)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewModel

        selectScaleItem = binding.backgroundScale

        setObserve()
        return binding.root
    }

    private fun setObserve(){
        viewModel.selectBackgroundItem.observe(viewLifecycleOwner){
            selectScaleItem.setBackgroundColor(0x00FF0000)
            when(it){
                0->{
                    binding.scale11.setBackgroundResource(R.drawable.bg_selectitem)
                    selectScaleItem = binding.scale11
                }
                1->{
                    binding.scale43.setBackgroundResource(R.drawable.bg_selectitem)
                    selectScaleItem = binding.scale43
                }
                2->{
                    binding.scale34.setBackgroundResource(R.drawable.bg_selectitem)
                    selectScaleItem = binding.scale34
                }
                3->{
                    binding.scale32.setBackgroundResource(R.drawable.bg_selectitem)
                    selectScaleItem = binding.scale32
                }
                4->{
                    binding.scale23.setBackgroundResource(R.drawable.bg_selectitem)
                    selectScaleItem = binding.scale23
                }
                5->{
                    binding.scale169.setBackgroundResource(R.drawable.bg_selectitem)
                    selectScaleItem = binding.scale169
                }
                6->{
                    binding.scale916.setBackgroundResource(R.drawable.bg_selectitem)
                    selectScaleItem = binding.scale916
                }
            }
        }
    }

}