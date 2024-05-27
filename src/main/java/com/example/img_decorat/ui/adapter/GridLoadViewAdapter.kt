package com.example.img_decorat.ui.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.img_decorat.R
import com.example.img_decorat.data.model.dataModels.LoadData
import com.example.img_decorat.databinding.ItemGridLoadDataBinding
import com.example.img_decorat.databinding.ItemLinearLoadDataBinding

class GridLoadViewAdapter(val list : List<LoadData>, val onItemClickListener: OnGridLoadItemClickListener): RecyclerView.Adapter<GridLoadViewAdapter.GridLoadViewHolder>() {

    private var selectedPosition = -1

    interface OnGridLoadItemClickListener{
        fun onGridLoadItemClick(position: Int, clickedItem: Int)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridLoadViewHolder
            = GridLoadViewHolder(ItemGridLoadDataBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: GridLoadViewHolder, position: Int) {
        holder.setName(list[position].title)
        holder.setView(list[position].titleImage)
        holder.clickedDelete(position)
        holder.clickedItem(position)
    }

    inner class GridLoadViewHolder(val binding: ItemGridLoadDataBinding): RecyclerView.ViewHolder(binding.root){
        fun setName(name: String){
            binding.loadName.text = name
        }

        fun setView(bitmap: Bitmap){
            Glide.with(binding.root).load(bitmap).into(binding.loadView)
            binding.itemGridLoadData.setBackgroundColor(Color.TRANSPARENT)
        }

        fun clickedDelete(position: Int){
            binding.loadItemDelete.setOnClickListener {
                onItemClickListener.onGridLoadItemClick(position = position, clickedItem = 1)
            }
        }

        fun clickedItem(position: Int){
            binding.itemGridLoadData.setOnClickListener {
                onItemClickListener.onGridLoadItemClick(position = position, clickedItem = 0)
                val existingPosition = selectedPosition
                val border = GradientDrawable()
                border.setColor(0xFFC0C0C0.toInt())
                border.cornerRadius = 20f

                selectedPosition = position
                binding.itemGridLoadData.background = border
                notifyItemChanged(existingPosition)
            }
        }
    }
}