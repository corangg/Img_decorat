package com.example.img_decorat.ui.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.img_decorat.R
import com.example.img_decorat.data.model.dataModels.unsplashimagedata.UnsplashData
import com.example.img_decorat.databinding.ItemImageBinding
import com.example.img_decorat.utils.UtilList

class ImageAdapter(val imageList: List<UnsplashData>, val onItemClickListener:OnImageItemClickListener):RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private var selectedPosition = -1

    interface OnImageItemClickListener{
        fun onImageItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder
    = ImageViewHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.setImage(position)
        holder.clieckedItem(position)
    }

    inner class ImageViewHolder(val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root){
        fun setImage(position: Int){
            val bitmapUrl = imageList[position].urls.small
            Glide.with(binding.root).load(bitmapUrl).override(60,100).into(binding.backgroundImageItem)
        }

        fun clieckedItem(position: Int){
            binding.backgroundImageItem.setOnClickListener {
                onItemClickListener.onImageItemClick(position)
                val existingPosition = selectedPosition
                val border = GradientDrawable()
                border.setColor(Color.LTGRAY)

                selectedPosition = position
                binding.backgroundImageItem.background = border
                notifyItemChanged(existingPosition)
            }
        }
    }
}
