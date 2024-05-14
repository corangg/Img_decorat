package com.example.img_decorat

import com.example.img_decorat.dataModels.UnsplashData
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
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