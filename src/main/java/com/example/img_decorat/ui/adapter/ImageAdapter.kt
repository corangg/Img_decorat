package com.example.img_decorat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.img_decorat.R
import com.example.img_decorat.dataModels.UnsplashData
import com.example.img_decorat.databinding.ItemColorBinding
import com.example.img_decorat.databinding.ItemImageBinding


class ImageViewHolder(val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root)
class ImageAdapter(val imageList: MutableList<UnsplashData>, val onItemClickListener:OnColorItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnColorItemClickListener{
        fun onImageItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = ImageViewHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as ImageViewHolder).binding

        Glide.with(binding.root).load(imageList[position].urls.small).into(binding.imageItem)

        binding.imageItem.setOnClickListener {
            onItemClickListener.onImageItemClick(position)
        }
    }
}
