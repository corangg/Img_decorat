package com.example.img_decorat.apiinterface

import com.example.img_decorat.dataModels.unsplashimagedata.UnsplashData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApiService {
    @GET(
        "photos/random/?" +
                "client_id=bGDxV3cKfy2rku6MPVgrSQw9IYSKRQ7mJbDOZsC0Ons" +
                "&count=20"
    )
    suspend fun getSearchedPhotos(
        @Query("query") query: String?
    ):Response<List<UnsplashData>>
}