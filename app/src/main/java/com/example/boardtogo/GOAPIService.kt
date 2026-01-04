package com.example.boardtogo

import retrofit2.http.GET
import retrofit2.http.Query

interface GOAPIService {
    @GET("api/V1/ServiceUpdate/UnionDepartures/All")
    suspend fun getUnionDepartures(
        @Query("key") apiKey: String = BuildConfig.GO_KEY,
    ): UnionDeparturesResponse
}