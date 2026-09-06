package com.example.newsreader.data.mapper

import com.example.newsreader.data.remote.dto.GNewsArticleDto
import com.example.newsreader.domain.model.NewsArticle
import com.example.newsreader.domain.model.NewsCategory
import java.time.Instant

fun GNewsArticleDto.toNewsArticle(
    category: NewsCategory
): NewsArticle {
    return NewsArticle(
        id = id,
        title = title,
        description = description,
        url = url,
        publishedAt = Instant.parse(publishedAt),
        sourceName = source.name,
        sourceUrl = source.url,
        category = category
    )
}
