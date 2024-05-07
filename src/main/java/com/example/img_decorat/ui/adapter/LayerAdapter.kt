package com.example.img_decorat.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.img_decorat.databinding.ItemLayerBinding
import com.example.img_decorat.databinding.ItemMenuBinding

class LayerViewHolder(val binding: ItemLayerBinding): RecyclerView.ViewHolder(binding.root){
    fun bindLayer(uri: Uri){
        if(uri != null){
            Glide.with(binding.root).load(uri).into(binding.layerImg)
        }

    }
}

class LayerAdapter(val layerList: MutableList<Uri>, val onItemClickListener: LayerAdapter.OnItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener{//드래그 할꺼라 바꿔야 할ㄷ스
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return layerList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = LayerViewHolder(ItemLayerBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as LayerViewHolder).binding

        holder.bindLayer(layerList[position])
    }

}
/*
class LayerViewHolder(val binding: ItemMenuBinding): RecyclerView.ViewHolder(binding.root)

class LayerAdapter(val menuList: MutableList<String>, val onItemClickListener: OnItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = LayerViewHolder(ItemMenuBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as LayerViewHolder).binding

        binding.menuText.text = menuList[position]

        binding.itemMenu.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }
}*/
