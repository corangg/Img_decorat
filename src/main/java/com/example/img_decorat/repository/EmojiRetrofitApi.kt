package com.example.img_decorat.repository

import com.example.img_decorat.apiinterface.EmojiApiService
import com.example.img_decorat.dataModels.EmojiData
import com.example.img_decorat.utils.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object EmojiRetrofitApi {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://emoji-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: EmojiApiService by lazy {
        retrofit.create(EmojiApiService::class.java)
    }
}