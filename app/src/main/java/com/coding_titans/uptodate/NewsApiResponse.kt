package com.coding_titans.uptodate

data class NewsApiResponse(
    val status: String,
    val news: List<NewsArticle>
)

data class NewsArticle(
    val title: String,
    val description: String,
    val url: String,
    val image: String
)
