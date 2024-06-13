package com.example.img_decorat.utils

interface ItemClickInterface {
    fun onColorItemClick(position: Int, case: Int){}

    fun onItemClick(position: Int) {}

    fun onLoadItemClick(position: Int, clickedItem: Int) {}

    fun onCheckedClick(position: Int, checked : Boolean){}

    fun onDeleteItemClick(position: Int) {}

    fun onMenuItemClick(position: Int) {}

}