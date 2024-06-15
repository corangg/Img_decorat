package com.example.img_decorat.data.repository

import com.example.img_decorat.data.model.dataModels.Hue
import com.example.img_decorat.data.model.dataModels.ListData
import com.example.img_decorat.data.model.dataModels.ViewItemData
import com.example.img_decorat.utils.Util.getLastSelectView
import java.util.LinkedList

class HueRepositoryImpl {
    fun checkHue(viewList: LinkedList<ViewItemData>, id: Int, hue: Hue): Hue?{
        getLastSelectView(viewList, id)?.let {
            return Hue(checkSaturatio(it, hue.saturatio), checkBrightness(it, hue.brightness), checkTransparency(it, hue.transparency))
        }
        return null
    }

    fun editViewSaturation(listData: ListData, saturation: Int) : LinkedList<ViewItemData> {
        listData.layerList.find { it.select }?.let { item ->
            listData.viewList.find { it.id == item.id }?.let {
                it.saturation = saturation
                when (it.type) {
                    0 -> {
                        it.img.setImageSaturation(saturation.toFloat())
                    }

                    1 -> {
                        it.text.setSaturation(saturation.toFloat())
                    }
                }
            }
        }
        return listData.viewList
    }

    fun editImageViewBrightness(listData: ListData, brightness: Int) : LinkedList<ViewItemData> {
        listData.layerList.find { it.select }?.let { item ->
            listData.viewList.find { it.id == item.id }?.let {
                it.brightness = brightness
                when (it.type) {
                    0 -> {
                        it.img.setImageBrightness(brightness.toFloat())
                    }

                    1 -> {
                        it.text.setBrightness(brightness.toFloat())
                    }
                }
            }
        }
        return listData.viewList
    }

    fun editViewTransparency(listData: ListData, transparency: Int) : LinkedList<ViewItemData>  {
        listData.layerList.find { it.select }?.let { item ->
            listData.viewList.find { it.id == item.id }?.let {
                it.transparency = transparency
                when (it.type) {
                    0 -> {
                        it.img.setImageTransparency(transparency.toFloat())
                    }

                    1 -> {
                        it.text.setTransparency(transparency.toFloat())
                    }
                }
            }
        }
        return listData.viewList
    }

    private fun checkSaturatio(viewItemData: ViewItemData, saturatio: Int): Int{
        return if(viewItemData.saturation != saturatio){
            viewItemData.saturation
        }else{
            saturatio
        }
    }

    private fun checkBrightness(viewItemData: ViewItemData, brightness: Int): Int{
        return if(viewItemData.brightness != brightness){
            viewItemData.brightness
        }else{
            brightness
        }
    }

    private fun checkTransparency(viewItemData: ViewItemData, transparency: Int): Int{
        return if(viewItemData.transparency != transparency){
            viewItemData.transparency
        }else{
            transparency
        }
    }
}