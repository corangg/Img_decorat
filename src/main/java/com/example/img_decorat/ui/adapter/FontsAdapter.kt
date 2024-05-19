package com.example.img_decorat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.img_decorat.dataModels.Font
import com.example.img_decorat.databinding.ItemEmojiBinding
import com.example.img_decorat.databinding.ItemFontBinding


class FontsViewHolder(val binding: ItemFontBinding): RecyclerView.ViewHolder(binding.root){
    fun applyFont(font: Font){


    }
}
class FontsAdapter(val fontsList : List<Font>,val onItemClickListener: OnFontItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        holder.applyFont(fontsList[position])
        binding.fontItem.setOnClickListener {
            onItemClickListener.onFontItemClick(position)
        }

    }
}