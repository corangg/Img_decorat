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
import com.example.img_decorat.databinding.FragmentTextBinding
import com.example.img_decorat.viewmodel.MainViewModel

class TextFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentTextBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_text,container,false)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewModel

        setObserve()
        return binding.root
    }
    private fun setObserve(){
        viewModel.selectTextMenu.observe(viewLifecycleOwner){
            when(it){
                0->{
                    parentFragmentManager.beginTransaction().replace(binding.textItemView.id,
                        TextFontFragment()
                    ).commit()
                }
                1->{
                    parentFragmentManager.beginTransaction().replace(binding.textItemView.id,
                        TextColorFragment()
                    ).commit()
                }
                2->{
                    parentFragmentManager.beginTransaction().replace(binding.textItemView.id,
                        TextSizeFragment()
                    ).commit()
                }
            }
        }

    }

}