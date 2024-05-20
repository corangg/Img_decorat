package com.example.img_decorat.repository

import com.example.img_decorat.apiinterface.UnsplashApiService
import com.example.img_decorat.data.model.dataModels.unsplashimagedata.UnsplashData
import com.example.img_decorat.utils.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitApi {
    private val unsplashApiService: UnsplashApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.UNSPLASH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
    suspend fun getRandomPhotos(query: String?): List<UnsplashData>? =
        unsplashApiService.getSearchedPhotos(query).body()
}