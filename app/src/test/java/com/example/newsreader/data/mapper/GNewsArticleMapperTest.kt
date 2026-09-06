package com.example.newsreader.data.mapper

import com.example.newsreader.data.remote.dto.GNewsArticleDto
import com.example.newsreader.data.remote.dto.GNewsSourceDto
import com.example.newsreader.domain.model.NewsCategory
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class GNewsArticleMapperTest {

    @Test
    fun toNewsArticle_mapsEveryRequiredProperty() {
        val dto = GNewsArticleDto(
            id = "article-id",
            title = "日本経済のニュース",
            description = "記事の説明",
            url = "https://example.com/article",
            publishedAt = "2026-09-06T00:00:00Z",
            lang = "ja",
            source = GNewsSourceDto(
                name = "Example News",
                url = "https://example.com"
            )
        )

        val result = dto.toNewsArticle(NewsCategory.BUSINESS)

        assertEquals("article-id", result.id)
        assertEquals("日本経済のニュース", result.title)
        assertEquals("記事の説明", result.description)
        assertEquals("https://example.com/article", result.url)
        assertEquals(
            Instant.parse("2026-09-06T00:00:00Z"),
            result.publishedAt
        )
        assertEquals("Example News", result.sourceName)
        assertEquals("https://example.com", result.sourceUrl)
        assertEquals(NewsCategory.BUSINESS, result.category)
    }
}
