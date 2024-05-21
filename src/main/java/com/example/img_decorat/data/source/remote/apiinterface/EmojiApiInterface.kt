package com.example.img_decorat.data.source.remote.apiinterface

import com.example.img_decorat.data.model.dataModels.EmojiData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EmojiApiInterface {
    @GET("emojis")
    suspend fun getEmojis(@Query("access_key") apiKey: String): Response<List<EmojiData>>
}