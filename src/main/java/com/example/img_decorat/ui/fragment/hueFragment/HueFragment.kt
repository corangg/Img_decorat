package com.example.img_decorat.ui.fragment.hueFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentBackGroundBinding
import com.example.img_decorat.databinding.FragmentHueBinding
import com.example.img_decorat.viewmodel.MainViewModel

class HueFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentHueBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hue,container,false)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewModel

        setObserve()
        return binding.root

        return inflater.inflate(R.layout.fragment_hue, container, false)
    }

    private fun setObserve(){

    }

}