package com.example.img_decorat.ui.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.img_decorat.data.model.dataModels.LoadData
import com.example.img_decorat.databinding.ItemLinearLoadDataBinding


class LinearLoadViewAdapter(val list : List<LoadData>, val onItemClickListener: OnLinearLoadItemClickListener): RecyclerView.Adapter<LinearLoadViewAdapter.LinearLoadViewHolder>() {
    private var selectedPosition = -1

    interface OnLinearLoadItemClickListener{
        fun onLinearLoadItemClick(position: Int, clickedItem: Int)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinearLoadViewHolder
    = LinearLoadViewHolder(ItemLinearLoadDataBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: LinearLoadViewHolder, position: Int) {
        holder.setName(list[position].title)
        holder.setView(list[position].titleImage)
        holder.clickedDelete(position)
        holder.clickedItem(position)
    }

    inner class LinearLoadViewHolder(val binding: ItemLinearLoadDataBinding): RecyclerView.ViewHolder(binding.root){
        fun setName(name: String){
            binding.loadName.text = name
        }

        fun setView(bitmap: Bitmap){
            Glide.with(binding.root).load(bitmap).into(binding.loadView)
            binding.itemLinearLoadData.setBackgroundColor(Color.TRANSPARENT)
        }

        fun clickedDelete(position: Int){
            binding.loadItemDelete.setOnClickListener {
                onItemClickListener.onLinearLoadItemClick(position = position, clickedItem = 1)
            }
        }

        fun clickedItem(position: Int){
            binding.itemLinearLoadData.setOnClickListener {
                onItemClickListener.onLinearLoadItemClick(position = position, clickedItem = 0)
                val existingPosition = selectedPosition
                val border = GradientDrawable()
                border.setColor(0xFFC0C0C0.toInt())
                border.cornerRadius = 20f

                selectedPosition = position
                binding.itemLinearLoadData.background = border
                notifyItemChanged(existingPosition)
            }
        }
    }

}