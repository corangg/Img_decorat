package com.example.img_decorat.data.source.remote.retrofit

import com.example.img_decorat.data.source.remote.apiinterface.EmojiApiInterface
import com.example.img_decorat.utils.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EmojiRetrofit {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.EMOJI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: EmojiApiInterface by lazy {
        retrofit.create(EmojiApiInterface::class.java)
    }
}