package com.example.img_decorat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.img_decorat.databinding.ItemMenuBinding

class MenuAdapter(val menuList: List<String>, val onItemClickListener: OnItemClickListener):RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(){

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder
        = MenuViewHolder(ItemMenuBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.setTitle(position)
        holder.clickedItem(position)
    }

    inner class MenuViewHolder(val binding: ItemMenuBinding): RecyclerView.ViewHolder(binding.root){
        fun setTitle(position: Int){
            binding.menuText.text = menuList[position]
        }

        fun clickedItem(position: Int){
            binding.itemMenu.setOnClickListener {
                onItemClickListener.onItemClick(position)
            }
        }

    }
}