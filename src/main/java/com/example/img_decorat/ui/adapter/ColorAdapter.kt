package com.example.img_decorat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.img_decorat.databinding.ItemColorBinding
import com.example.img_decorat.databinding.ItemMenuBinding


class ColorViewHolder(val binding: ItemColorBinding): RecyclerView.ViewHolder(binding.root)

class ColorAdapter(val colorList: MutableList<Int>, val onItemClickListener: OnColorItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

   interface OnColorItemClickListener{
       fun onColorItemClick(position: Int)
   }

    override fun getItemCount(): Int {
        return colorList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = ColorViewHolder(ItemColorBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as ColorViewHolder).binding
        binding.colorItem.setBackgroundColor(colorList[position])

        binding.colorItem.setOnClickListener {
            onItemClickListener.onColorItemClick(position)
        }
    }
}