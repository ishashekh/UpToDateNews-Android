package com.coding_titans.uptodate.api

data class NewsApiJSON(
    val news: List<New>,
    val page: Int,
    val status: String
)