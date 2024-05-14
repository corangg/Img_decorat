package com.example.img_decorat.ui.fragment

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
import com.example.img_decorat.ui.activity.MainActivity
import com.example.img_decorat.viewmodel.MainViewModel

class BackGroundFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentBackGroundBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_back_ground,container,false)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewModel

        setObserve()
        return binding.root
    }

    private fun setObserve(){

        viewModel.selectbackgroundMenu.observe(viewLifecycleOwner){
            when(it){
                0->{
                    parentFragmentManager.beginTransaction().replace(binding.backgroundItemView.id,BackGroundScaleFragment()).commit()
                }
                1->{
                    parentFragmentManager.beginTransaction().replace(binding.backgroundItemView.id,BackGroundColorFragment()).commit()
                }
            }
        }

    }
}