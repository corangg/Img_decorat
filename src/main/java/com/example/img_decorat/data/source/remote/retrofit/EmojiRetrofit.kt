package com.example.img_decorat.data.source.remote.retrofit

import com.example.img_decorat.data.source.remote.apiinterface.EmojiApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EmojiRetrofit {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://emoji-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: EmojiApiInterface by lazy {
        retrofit.create(EmojiApiInterface::class.java)
    }
}