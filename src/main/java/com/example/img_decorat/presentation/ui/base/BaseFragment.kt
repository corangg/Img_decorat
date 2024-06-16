package com.example.img_decorat.presentation.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.img_decorat.presentation.ui.view.BTNAnimation
import javax.inject.Inject

abstract class BaseFragment<B : ViewDataBinding, VM : ViewModel> : Fragment() {
    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    @Inject
    lateinit var animation: BTNAnimation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId(), container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(requireActivity()).get(getViewModelClass())

        initializeUI()

        return binding.root
    }

    abstract fun getViewModelClass(): Class<VM>

    abstract fun layoutResId(): Int

    abstract fun initializeUI()

    protected fun replaceFragment(id: Int, fragment: Fragment, backStack: Boolean = false) {
        val beginFragment = parentFragmentManager.beginTransaction().replace(id, fragment)
        if (backStack) {
            beginFragment.addToBackStack(null).commit()
        } else {
            beginFragment.commit()
        }
    }


    fun getTransaction(): FragmentTransaction {
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        return transaction
    }
}