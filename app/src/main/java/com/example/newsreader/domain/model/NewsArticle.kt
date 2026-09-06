package com.example.newsreader.domain.model

import java.time.Instant

data class NewsArticle(
    val id: String,
    val title: String,
    val description: String?,
    val url: String,
    val publishedAt: Instant,
    val sourceName: String,
    val sourceUrl: String,
    val category: NewsCategory
)
