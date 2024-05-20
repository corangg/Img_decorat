package com.example.img_decorat.apiinterface

import com.example.img_decorat.data.model.dataModels.EmojiData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EmojiApiService {
    @GET("emojis")
    suspend fun getEmojis(@Query("access_key") apiKey: String): Response<List<EmojiData>>
}