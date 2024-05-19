package com.example.img_decorat.repository

import com.example.img_decorat.apiinterface.GoogleFontsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GoogleFontsRetrofitApi {
    private const val BASE_URL = "https://www.googleapis.com/webfonts/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val googleFontsApi: GoogleFontsApiService = retrofit.create(GoogleFontsApiService::class.java)
}