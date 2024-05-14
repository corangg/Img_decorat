package com.example.img_decorat

import com.example.img_decorat.dataModels.UnsplashData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Url

object RetrofitApi {
    private val unsplashApiService: UnsplashApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Util.UNSPLASH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
    // 실제로 호출할 수 있는 호출부 구현
    suspend fun getRandomPhotos(query: String?): List<UnsplashData>? =
        unsplashApiService.getSearchedPhotos(query).body()

    // 로깅 찍기 위해 OkHttpClient 추가
    private fun buildOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
}