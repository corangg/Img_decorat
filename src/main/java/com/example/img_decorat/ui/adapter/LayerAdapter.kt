package com.example.img_decorat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.img_decorat.ImgLayerData
import com.example.img_decorat.databinding.ItemLayerBinding
import java.util.Collections
import java.util.LinkedList

class LayerViewHolder(val binding: ItemLayerBinding): RecyclerView.ViewHolder(binding.root){
    fun bindLayer(layerData: ImgLayerData, position: Int){
        val num = position + 1
        binding.layerNum.text = num.toString()

        if(layerData.uri != null){
            Glide.with(binding.root).load(layerData.uri).into(binding.layerImg)
        }

        binding.check.isChecked = layerData.check//체크되면 체크된 값 리턴해야할듯
    }
}

class LayerAdapter(val layerList: LinkedList<ImgLayerData>, val onLayerItemClickListener: OnLayerItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnLayerItemClickListener{//드래그 할꺼라 바꿔야 할ㄷ스
        fun onCheckedClick(position: Int, checked : Boolean)

        fun onLayerDelete(position: Int)
    }

    override fun getItemCount(): Int {
        return layerList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = LayerViewHolder(ItemLayerBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as LayerViewHolder).binding

        holder.bindLayer(layerList[position], position)

        binding.check.setOnClickListener {
            onLayerItemClickListener.onCheckedClick(position, binding.check.isChecked)
        }

        binding.layerDelete.setOnClickListener {
            onLayerItemClickListener.onLayerDelete(position)
        }
    }
}
