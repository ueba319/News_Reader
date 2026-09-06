package com.example.newsreader.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GNewsResponseDto(
    val totalArticles: Int,
    val articles: List<GNewsArticleDto>
)

@Serializable
data class GNewsArticleDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val url: String,
    val publishedAt: String,
    val lang: String,
    val source: GNewsSourceDto
)

@Serializable
data class GNewsSourceDto(
    val name: String,
    val url: String
)