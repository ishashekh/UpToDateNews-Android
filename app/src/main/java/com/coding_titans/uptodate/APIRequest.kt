package com.coding_titans.uptodate

import com.coding_titans.uptodate.api.NewsApiJSON
import retrofit2.http.GET

interface APIRequest {

    @GET("/v1/latest-news?language=en&apiKey=WPTVRN2UoZYACLhBejlWMTNkEOQ4r-lYpBymUItdnBw4s5gh")
    suspend fun getNews(): NewsApiJSON

}