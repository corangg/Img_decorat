package com.example.img_decorat.ui.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.img_decorat.databinding.ItemColorBinding

class ColorAdapter(val colorList: MutableList<Int>, val onItemClickListener: OnColorItemClickListener, val case: Int):RecyclerView.Adapter<ColorAdapter.ColorViewHolder>(){
    private var selectedPosition = -1
   interface OnColorItemClickListener{
       fun onColorItemClick(position: Int, case: Int)
   }

    override fun getItemCount(): Int {
        return colorList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder
    = ColorViewHolder(ItemColorBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.setBackgroundColor(colorList[position])
        holder.clickedItem(position)
    }

    inner class ColorViewHolder(val binding: ItemColorBinding) : RecyclerView.ViewHolder(binding.root){
        fun setBackgroundColor(color: Int){
            binding.colorItem.setBackgroundColor(color)
        }

        fun clickedItem(position: Int){
            binding.colorItem.setOnClickListener {
                onItemClickListener.onColorItemClick(position, case)
                val existingPosition = selectedPosition
                val border = GradientDrawable()
                border.setColor(colorList[position])
                border.setStroke(4, android.graphics.Color.WHITE)

                selectedPosition = position
                binding.colorItem.background = border
                notifyItemChanged(existingPosition)
            }
        }
    }
}