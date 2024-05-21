package com.example.img_decorat.data.repository

import android.graphics.Bitmap
import java.util.Stack

class SplitStackRepository {

    val stackPrevious = Stack<Bitmap>()
    val stackNext = Stack<Bitmap>()

    fun addPreviousStack(bitmap: Bitmap?){
        if(bitmap!=null){
            stackPrevious.push(bitmap)
        }
    }

    fun addNextStack(bitmap: Bitmap?){
        if(bitmap!=null){
            stackNext.push(bitmap)
        }
    }

    fun clearPreviousStack(){
        stackPrevious.clear()
    }
    fun clearNextStack(){
        stackNext.clear()
    }

    fun checkPreviousStackState():Boolean{
        return if(stackPrevious.size>0){
            true
        }else{
            false
        }
    }
    fun checkNextStackState():Boolean{
        return if(stackNext.size>0){
            true
        }else{
            false
        }
    }

    fun popPrevious():Bitmap{
        return stackPrevious.pop()
    }

    fun popNext():Bitmap{
        return stackNext.pop()
    }
}