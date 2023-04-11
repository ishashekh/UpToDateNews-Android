package com.coding_titans.uptodate

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentsApiService {
    @GET("latest-news")
    suspend fun getNewsForCountry(
        @Query("country") countryCode: String,
        @Query("apiKey") apiKey: String
    ): NewsApiResponse
}
