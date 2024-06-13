package com.example.img_decorat.data.source.remote.retrofit

import com.example.img_decorat.BuildConfig
import com.example.img_decorat.data.model.dataModels.EmojiData
import com.example.img_decorat.data.source.remote.apiinterface.EmojiApiInterface
import com.example.img_decorat.utils.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object EmojiRetrofit {
    private val emojiInterface : EmojiApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.EMOJI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    suspend fun getEmojis(): List<EmojiData>? =
        emojiInterface.getEmojis(BuildConfig.EMOJI_API_KEY).body()
}