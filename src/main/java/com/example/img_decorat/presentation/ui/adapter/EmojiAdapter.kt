package com.example.img_decorat.presentation.ui.adapter

import android.graphics.Bitmap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.img_decorat.databinding.ItemEmojiBinding
import com.example.img_decorat.utils.ItemClickInterface

class EmojiAdapter(
    val emojiList: MutableList<Bitmap>,
    val onItemClickListener: ItemClickInterface
) : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

    override fun getItemCount(): Int {
        return emojiList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder =
        EmojiViewHolder(
            ItemEmojiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {

        holder.emojiSet(emojiList[position])
        holder.clickedItem(position)
    }

    inner class EmojiViewHolder(val binding: ItemEmojiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun emojiSet(character: Bitmap) {
            Glide.with(binding.root).load(character).into(binding.emojiItem)
        }

        fun clickedItem(position: Int) {
            binding.emojiItem.setOnClickListener {
                onItemClickListener.onItemClick(position)
            }
        }

    }
}