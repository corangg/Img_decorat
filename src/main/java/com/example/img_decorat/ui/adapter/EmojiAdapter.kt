package com.example.img_decorat.ui.adapter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.img_decorat.dataModels.EmojiData
import com.example.img_decorat.databinding.ItemColorBinding
import com.example.img_decorat.databinding.ItemEmojiBinding


class EmojiViewHolder(val binding: ItemEmojiBinding): RecyclerView.ViewHolder(binding.root){
    fun emojiSet(character:Bitmap){

        Glide.with(binding.root).load(character).into(binding.emojiItem)
    }

}
class EmojiAdapter(val emojiList: MutableList<Bitmap>, val onItemClickListener: OnEmojiItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnEmojiItemClickListener{
        fun onEmojiItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return emojiList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = EmojiViewHolder(ItemEmojiBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as EmojiViewHolder).binding
        holder.emojiSet(emojiList[position])
        binding.emojiItem.setOnClickListener {
            onItemClickListener.onEmojiItemClick(position)
        }
    }
}