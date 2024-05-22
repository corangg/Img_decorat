package com.example.img_decorat.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.viewbinding.ViewBinding
import com.example.img_decorat.R
import com.example.img_decorat.ui.view.BTNAnimation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.security.auth.callback.Callback

abstract class BaseFragment<B : ViewDataBinding>: Fragment() {
    protected lateinit var binding : B
    @Inject lateinit var animation: BTNAnimation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId(),container,false)
        (binding as ViewDataBinding).lifecycleOwner = this

        initializeUI()
        setObserve()

        return binding.root
    }

    abstract fun layoutResId(): Int

    abstract fun initializeUI()

    abstract fun setObserve()

    fun fragmentClose(){
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun getTransaction():FragmentTransaction{
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        return transaction
    }


}