package com.example.img_decorat.data.source.remote.apiinterface

import com.example.img_decorat.data.model.dataModels.unsplashimagedata.UnsplashData
import com.example.img_decorat.utils.APIKey
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApiInterface {
    @GET(
        "photos/random/?" + APIKey.unsplashApiKey + "&count=20"
    )
    suspend fun getSearchedPhotos(
        @Query("query") query: String?
    ):Response<List<UnsplashData>>
}