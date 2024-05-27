package com.example.img_decorat.ui.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.fonts.Font
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.img_decorat.R

import com.example.img_decorat.databinding.ItemEmojiBinding
import com.example.img_decorat.databinding.ItemFontBinding

class FontsAdapter(val fontsList : List<Typeface>,val onItemClickListener: OnFontItemClickListener):RecyclerView.Adapter<FontsAdapter.FontsViewHolder>() {
    private var selectedPosition = -1

    interface OnFontItemClickListener{
        fun onFontItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return fontsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontsViewHolder
            = FontsViewHolder(ItemFontBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: FontsViewHolder, position: Int) {
        holder.applyFont(fontsList[position])
        holder.clickedItem(position)
    }

    inner class FontsViewHolder(val binding: ItemFontBinding): RecyclerView.ViewHolder(binding.root){
        fun applyFont(font: Typeface){
            binding.fontItem.typeface = font
            binding.fontItem.setBackgroundColor(Color.TRANSPARENT)
        }

        fun clickedItem(position: Int){
            binding.fontItem.setOnClickListener {
                onItemClickListener.onFontItemClick(position)
                val existingPosition = selectedPosition
                val border = GradientDrawable()
                border.setColor(Color.TRANSPARENT)
                border.cornerRadius = 12f
                border.setStroke(4, 0xFFE3CDF2.toInt())

                selectedPosition = position
                binding.fontItem.background = border
                notifyItemChanged(existingPosition)
            }
        }
    }
}