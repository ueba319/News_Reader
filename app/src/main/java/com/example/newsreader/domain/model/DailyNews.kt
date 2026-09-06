package com.example.newsreader.domain.model

data class DailyNews(
    val domesticArticles: List<NewsArticle>,
    val worldArticles: List<NewsArticle>
)