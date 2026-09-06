package com.example.newsreader.data.repository

import com.example.newsreader.domain.model.NewsArticle
import com.example.newsreader.domain.model.NewsCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class NewsArticleSelectorTest {

    @Test
    fun prioritizeByKeywords_movesMatchingArticlesToTheFront() {
        val unrelatedArticle = article(
            id = "unrelated",
            source = "source-a",
            category = NewsCategory.BUSINESS
        )
        val matchingArticle = article(
            id = "matching",
            source = "source-b",
            category = NewsCategory.BUSINESS
        ).copy(title = "日銀が金利政策を発表")

        val result = NewsArticleSelector.prioritizeByKeywords(
            articles = listOf(unrelatedArticle, matchingArticle),
            keywords = listOf("日銀", "金利")
        )

        assertEquals("matching", result.first().id)
        assertEquals("unrelated", result.last().id)
    }

    @Test
    fun selectBalanced_selectsThreeSituationAndTwoBusinessArticles() {
        val situationArticles = (1..5).map { index ->
            article(
                id = "situation-$index",
                source = "situation-source-$index",
                category = NewsCategory.NATION
            )
        }
        val businessArticles = (1..5).map { index ->
            article(
                id = "business-$index",
                source = "business-source-$index",
                category = NewsCategory.BUSINESS
            )
        }

        val result = NewsArticleSelector.selectBalanced(
            primaryArticles = situationArticles,
            secondaryArticles = businessArticles,
            maxArticles = 5,
            primaryQuota = 3
        )

        assertEquals(5, result.size)
        assertEquals(
            3,
            result.count { it.category == NewsCategory.NATION }
        )
        assertEquals(
            2,
            result.count { it.category == NewsCategory.BUSINESS }
        )
    }

    @Test
    fun selectBalanced_removesDuplicatesAndPrefersDifferentSources() {
        val duplicate = article(
            id = "duplicate",
            source = "source-a",
            category = NewsCategory.NATION
        )
        val primaryArticles = listOf(
            duplicate,
            article("primary-2", "source-a", NewsCategory.NATION),
            article("primary-3", "source-b", NewsCategory.NATION)
        )
        val secondaryArticles = listOf(
            duplicate.copy(category = NewsCategory.BUSINESS),
            article("secondary-2", "source-c", NewsCategory.BUSINESS),
            article("secondary-3", "source-d", NewsCategory.BUSINESS)
        )

        val result = NewsArticleSelector.selectBalanced(
            primaryArticles = primaryArticles,
            secondaryArticles = secondaryArticles,
            maxArticles = 4,
            primaryQuota = 2
        )

        assertEquals(4, result.size)
        assertEquals(result.size, result.map { it.id }.distinct().size)
        assertTrue(result.take(3).map { it.sourceName }.distinct().size >= 3)
    }

    private fun article(
        id: String,
        source: String,
        category: NewsCategory
    ): NewsArticle {
        return NewsArticle(
            id = id,
            title = "title-$id",
            description = "description-$id",
            url = "https://example.com/$id",
            publishedAt = Instant.parse("2026-09-06T00:00:00Z"),
            sourceName = source,
            sourceUrl = "https://example.com",
            category = category
        )
    }
}
