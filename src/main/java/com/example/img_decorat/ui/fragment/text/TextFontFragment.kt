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
import com.example.img_decorat.databinding.FragmentBackGroundColorBinding
import com.example.img_decorat.databinding.FragmentTextFontBinding
import com.example.img_decorat.ui.adapter.ColorAdapter
import com.example.img_decorat.ui.adapter.FontsAdapter
import com.example.img_decorat.utils.ColorList
import com.example.img_decorat.utils.FontsList
import com.example.img_decorat.viewmodel.MainViewModel

class TextFontFragment : Fragment(),FontsAdapter.OnFontItemClickListener {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentTextFontBinding
    lateinit var fontAdapter: FontsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_text_font,container,false)
        (binding as ViewDataBinding).lifecycleOwner = this
        binding.viewmodel = viewModel

        adapterSet()
        setObserve()
        return binding.root
    }

    override fun onFontItemClick(position: Int) {
        viewModel.textFontSet(position)
    }

    private fun adapterSet(){
        binding.textFont.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        fontAdapter = FontsAdapter(FontsList.typefaces, this)
        binding.textFont.adapter = fontAdapter
    }
    private fun setObserve(){

    }


}