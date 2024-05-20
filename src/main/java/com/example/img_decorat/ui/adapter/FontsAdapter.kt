package com.example.img_decorat.ui.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.fonts.Font
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.img_decorat.databinding.ItemEmojiBinding
import com.example.img_decorat.databinding.ItemFontBinding


class FontsViewHolder(val binding: ItemFontBinding): RecyclerView.ViewHolder(binding.root){
    fun applyFont(font: Typeface){
        binding.fontItem.typeface = font
    }
}
class FontsAdapter(val fontsList : List<Typeface>,val onItemClickListener: OnFontItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var selectedPosition = -1

    interface OnFontItemClickListener{
        fun onFontItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return fontsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = FontsViewHolder(ItemFontBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as FontsViewHolder).binding
        binding.fontItem.setBackgroundColor(Color.TRANSPARENT)
        holder.applyFont(fontsList[position])

        binding.fontItem.setOnClickListener {
            onItemClickListener.onFontItemClick(position)
            val positionSet =position
            val existingPosition = selectedPosition
            val border = GradientDrawable()
            border.setColor(Color.TRANSPARENT)
            border.cornerRadius = 12f
            border.setStroke(4, Color.WHITE)

            selectedPosition = positionSet
            binding.fontItem.background = border
            notifyItemChanged(existingPosition)
        }

    }
}