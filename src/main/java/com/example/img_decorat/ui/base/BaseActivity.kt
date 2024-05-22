package com.example.img_decorat.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.img_decorat.R
import com.example.img_decorat.ui.view.BTNAnimation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: B
    @Inject
    lateinit var animation: BTNAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId())
        (binding as ViewDataBinding).lifecycleOwner = this

        initializeUI()
        setObserve()
    }

    abstract fun layoutResId(): Int

    abstract fun initializeUI()

    abstract fun setObserve()
}