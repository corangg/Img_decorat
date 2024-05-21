package com.example.img_decorat.data.source.remote.retrofit

import com.example.img_decorat.data.model.dataModels.unsplashimagedata.UnsplashData
import com.example.img_decorat.data.source.remote.apiinterface.UnsplashApiInterface
import com.example.img_decorat.utils.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object UnsplashRetrofit {
    private val unsplashApiInterface: UnsplashApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.UNSPLASH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
    suspend fun getRandomPhotos(query: String?): List<UnsplashData>? =
        unsplashApiInterface.getSearchedPhotos(query).body()
}