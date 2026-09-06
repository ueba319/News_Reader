package com.example.newsreader.data.repository

import com.example.newsreader.domain.model.DailyNews
import com.example.newsreader.domain.model.NewsArticle
import com.example.newsreader.domain.model.NewsCategory
import com.example.newsreader.domain.repository.NewsRepository
import java.time.Instant
import javax.inject.Inject

class FakeNewsRepository @Inject constructor() : NewsRepository {

    override suspend fun getDailyNews(
        from: Instant,
        to: Instant
    ): DailyNews {
        return DailyNews(
            domesticArticles = listOf(
                article(
                    id = "domestic-test",
                    title = DOMESTIC_TEST_TITLE
                )
            ),
            worldArticles = listOf(
                article(
                    id = "world-test",
                    title = WORLD_TEST_TITLE
                )
            )
        )
    }

    private fun article(
        id: String,
        title: String
    ): NewsArticle {
        return NewsArticle(
            id = id,
            title = title,
            description = "テスト記事の説明",
            url = "https://example.com/$id",
            publishedAt = Instant.parse("2026-09-06T00:00:00Z"),
            sourceName = "Test News",
            sourceUrl = "https://example.com",
            category = NewsCategory.BUSINESS
        )
    }

    companion object {
        const val DOMESTIC_TEST_TITLE = "国内テストニュース"
        const val WORLD_TEST_TITLE = "World test news"
    }
}
