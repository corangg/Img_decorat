package com.example.img_decorat.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.img_decorat.dataModels.ImgLayerData
import com.example.img_decorat.databinding.ItemLayerBinding
import java.util.Collections
import java.util.LinkedList

class LayerViewHolder(val binding: ItemLayerBinding): RecyclerView.ViewHolder(binding.root){
    fun bindLayer(layerData: ImgLayerData, position: Int){
        val num = position + 1
        binding.layerNum.text = num.toString()

        Glide.with(binding.root).load(layerData.bitMap).into(binding.layerImg)

        binding.check.isChecked = layerData.check//체크되면 체크된 값 리턴해야할듯
    }
}

class LayerAdapter(val layerList: LinkedList<ImgLayerData>, val onLayerItemClickListener: OnLayerItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnLayerItemClickListener{//드래그 할꺼라 바꿔야 할ㄷ스
        fun onCheckedClick(position: Int, checked : Boolean)

        fun onLayerDelete(position: Int)

        fun onLayerItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return layerList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = LayerViewHolder(ItemLayerBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as LayerViewHolder).binding

        holder.bindLayer(layerList[position], position)

        binding.itemLayer.setBackgroundColor(if (layerList[position].select) 0xFF202020.toInt() else Color.TRANSPARENT)


        binding.itemLayer.setOnClickListener {
            onLayerItemClickListener.onLayerItemClick(position)
        }

        binding.check.setOnClickListener {
            onLayerItemClickListener.onCheckedClick(position, binding.check.isChecked)
        }

        binding.layerDelete.setOnClickListener {
            onLayerItemClickListener.onLayerDelete(position)
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
