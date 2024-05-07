package com.example.img_decorat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.img_decorat.databinding.ItemMenuBinding

class MenuViewHolder(val binding: ItemMenuBinding): RecyclerView.ViewHolder(binding.root)

class MenuAdapter(val menuList: MutableList<String>, val onItemClickListener: OnItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = MenuViewHolder(ItemMenuBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MenuViewHolder).binding

        binding.menuText.text = menuList[position]

        binding.itemMenu.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }
}