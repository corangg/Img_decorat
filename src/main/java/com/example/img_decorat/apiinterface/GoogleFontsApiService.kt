package com.example.img_decorat.apiinterface

import com.example.img_decorat.dataModels.FontsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleFontsApiService {
    @GET("v1/webfonts")
    fun getFonts(
        @Query("key") apiKey: String,
        @Query("sort") sort: String = "popularity"
    ): Call<FontsResponse>
}