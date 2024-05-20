package com.example.img_decorat.ui.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.img_decorat.databinding.ItemColorBinding
import com.example.img_decorat.databinding.ItemMenuBinding


class ColorViewHolder(val binding: ItemColorBinding): RecyclerView.ViewHolder(binding.root)

class ColorAdapter(val colorList: MutableList<Int>, val onItemClickListener: OnColorItemClickListener, val case: Int):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var selectedPosition = -1
   interface OnColorItemClickListener{
       fun onColorItemClick(position: Int,case: Int){
           true
       }
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
            onItemClickListener.onColorItemClick(position,case)
            val positionSet =position
            val existingPosition = selectedPosition
            val border = GradientDrawable()
            border.setColor(colorList[position])
            border.setStroke(4, android.graphics.Color.WHITE)

            selectedPosition = positionSet
            binding.colorItem.background = border
            notifyItemChanged(existingPosition)
        }
    }
}