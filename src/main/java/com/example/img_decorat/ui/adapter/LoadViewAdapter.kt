package com.example.img_decorat.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.img_decorat.data.model.dataModels.LoadData

import com.example.img_decorat.databinding.ItemImageBinding
import com.example.img_decorat.databinding.ItemLoadDataBinding
import com.example.img_decorat.utils.Util
import com.example.img_decorat.utils.UtilList
import org.w3c.dom.Text

class LoadViewAdapter(val list : List<LoadData>, val onImageItemClickListener: OnLoadItemClickListener): RecyclerView.Adapter<LoadViewAdapter.LoadViewHolder>() {
    private var selectedPosition = -1

    interface OnLoadItemClickListener{
        fun onLoadItemClick(position: Int, clickedItem: Int)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadViewHolder
    = LoadViewHolder(ItemLoadDataBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: LoadViewHolder, position: Int) {
        holder.setName(list[position].title)
        holder.setView(list[position].titleImage)
        holder.clickedDelete(position)
        holder.clickedItem(position)
    }

    inner class LoadViewHolder(val binding: ItemLoadDataBinding): RecyclerView.ViewHolder(binding.root){
        fun setName(name: String){
            binding.loadName.text = name
        }

        fun setView(bitmap: Bitmap){
            Glide.with(binding.root).load(bitmap).into(binding.loadView)
            binding.itemLoadData.setBackgroundColor(Color.TRANSPARENT)
        }

        fun clickedDelete(position: Int){
            binding.loadItemDelete.setOnClickListener {
                onImageItemClickListener.onLoadItemClick(position = position, clickedItem = 1)
            }
        }

        fun clickedItem(position: Int){
            binding.itemLoadData.setOnClickListener {
                onImageItemClickListener.onLoadItemClick(position = position, clickedItem = 0)
                val existingPosition = selectedPosition
                val border = GradientDrawable()
                border.setColor(Color.TRANSPARENT)
                border.setStroke(4, android.graphics.Color.WHITE)

                selectedPosition = position
                binding.itemLoadData.background = border
                notifyItemChanged(existingPosition)
            }
        }
    }

}