package com.example.img_decorat.presentation.ui.base

import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.img_decorat.utils.ItemClickInterface

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>(
    protected val items: List<T>, protected val onItemClickListener: ItemClickInterface
):RecyclerView.Adapter<VH>() {

    override fun getItemCount() = items.size

}