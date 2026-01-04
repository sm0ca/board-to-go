package com.example.boardtogo

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitClient {
    private const val BASE_URL = "https://api.openmetrolinx.com/OpenDataAPI/"
    val networkJson = Json { ignoreUnknownKeys = true }

    val goAPIService: GOAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(GOAPIService::class.java)
    }
}
