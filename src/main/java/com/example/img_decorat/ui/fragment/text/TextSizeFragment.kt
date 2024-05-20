package com.example.img_decorat.ui.fragment.text

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.example.img_decorat.R
import com.example.img_decorat.databinding.FragmentTextColorBinding
import com.example.img_decorat.databinding.FragmentTextFontBinding
import com.example.img_decorat.databinding.FragmentTextSizeBinding
import com.example.img_decorat.viewmodel.MainViewModel


class TextSizeFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentTextSizeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_text_size,container,false)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewModel


        return binding.root
    }


}