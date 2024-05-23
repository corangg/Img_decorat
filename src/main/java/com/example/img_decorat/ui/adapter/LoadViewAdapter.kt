package com.example.img_decorat.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.img_decorat.databinding.ItemImageBinding
import com.example.img_decorat.databinding.ItemLoadDataBinding
import com.example.img_decorat.utils.Util
import org.w3c.dom.Text

class LoadViewAdapter(private val context: Context, val list : List<String>, val onImageItemClickListener: OnLoadItemClickListener):
    RecyclerView.Adapter<LoadViewAdapter.LoadViewHolder>() {
    interface OnLoadItemClickListener{
        fun onLoadItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadViewHolder
    = LoadViewHolder(ItemLoadDataBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: LoadViewHolder, position: Int) {
        //holder.setName(list[position].name)
        //holder.setView(list[position].viewData)
    }

    inner class LoadViewHolder(val binding: ItemLoadDataBinding): RecyclerView.ViewHolder(binding.root){
        fun setName(name: String){
            binding.loadName.text = name
        }

        fun setView(dataString: String){
            Util.deserializeView(dataString,binding.loadView, context)
        }
    }

}