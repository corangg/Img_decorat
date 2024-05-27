package com.example.img_decorat.ui.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.img_decorat.data.model.dataModels.LayerItemData
import com.example.img_decorat.databinding.ItemLayerBinding
import java.util.Collections
import java.util.LinkedList

class LayerAdapter(val layerList: LinkedList<LayerItemData>, val onLayerItemClickListener: OnLayerItemClickListener):RecyclerView.Adapter<LayerAdapter.LayerViewHolder>() {
    interface OnLayerItemClickListener{
        fun onCheckedClick(position: Int, checked : Boolean)

        fun onLayerDelete(position: Int)

        fun onLayerItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return layerList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayerViewHolder
            = LayerViewHolder(ItemLayerBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: LayerViewHolder, position: Int) {
        holder.bindLayer(layerList[position], position)
        holder.setSlectedLayer(position)
        holder.clickedItem(position)
        holder.clickedVisibleCheck(position)
        holder.clickedDelete(position)
    }

    inner class LayerViewHolder(val binding: ItemLayerBinding): RecyclerView.ViewHolder(binding.root){
        fun bindLayer(layerItemData: LayerItemData, position: Int){
            val num = position + 1
            binding.layerNum.text = num.toString()
            binding.check.isChecked = layerItemData.check

            when(layerItemData.type){
                0->setImage(layerItemData.bitMap)
                1->setText(layerItemData)
            }
        }

        private fun setImage(bitmap: Bitmap){
            binding.layerImg.visibility = View.VISIBLE
            Glide.with(binding.root).load(bitmap).into(binding.layerImg)
        }

        private fun setText(layerItemData: LayerItemData){
            binding.layerText.visibility = View.VISIBLE

            binding.layerText.background = layerItemData.text.background
            binding.layerText.text = layerItemData.text.text
            binding.layerText.setTextColor(layerItemData.text.textColors)
            binding.layerText.typeface = layerItemData.text.typeface
        }

        fun setSlectedLayer(position: Int){
            binding.itemLayer.setBackgroundColor(if (layerList[position].select) 0xFFE3CDF2.toInt() else Color.TRANSPARENT)
        }

        fun clickedItem(position: Int){
            binding.itemLayer.setOnClickListener {
                onLayerItemClickListener.onLayerItemClick(position)
            }
        }

        fun clickedVisibleCheck(position: Int){
            binding.check.setOnClickListener {
                onLayerItemClickListener.onCheckedClick(position, binding.check.isChecked)
            }
        }

        fun clickedDelete(position: Int){
            binding.layerDelete.setOnClickListener {
                onLayerItemClickListener.onLayerDelete(position)
            }
        }
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {//여기는 괜찮나?
        Collections.swap(layerList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)

        for(i in 0 until layerList.size){
            notifyItemChanged(i)
        }
    }
}
