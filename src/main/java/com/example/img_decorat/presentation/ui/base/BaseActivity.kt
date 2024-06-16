package com.example.img_decorat.presentation.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.img_decorat.presentation.ui.view.BTNAnimation
import javax.inject.Inject

abstract class BaseActivity<B : ViewDataBinding, VM : ViewModel> : AppCompatActivity() {
    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    @Inject
    lateinit var animation: BTNAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId())
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(getViewModelClass())
        initializeUI()
        setObserve()
    }

    abstract fun getViewModelClass(): Class<VM>

    abstract fun layoutResId(): Int

    abstract fun initializeUI()

    abstract fun setObserve()

    protected fun replaceFragment(id: Int, fragment: Fragment, backStack: Boolean = false) {
        val beginFragment = supportFragmentManager.beginTransaction().replace(id, fragment)
        if (backStack) {
            beginFragment.addToBackStack(null).commit()
        } else {
            beginFragment.commit()
        }
    }

    protected fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    protected fun registerForActivityResultHandler(resultHandler: (ActivityResult) -> Unit) =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                resultHandler(it)
            }
        }

    protected fun setResultIntent(intent: Intent) {
        setResult(RESULT_OK, intent)
        finish()
    }
}