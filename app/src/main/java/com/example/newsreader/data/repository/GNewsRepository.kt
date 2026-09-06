package com.example.newsreader.data.repository

import com.example.newsreader.data.mapper.toNewsArticle
import com.example.newsreader.data.remote.api.GNewsApi
import com.example.newsreader.di.GNewsApiKey
import com.example.newsreader.domain.model.DailyNews
import com.example.newsreader.domain.model.NewsCategory
import com.example.newsreader.domain.repository.NewsRepository
import kotlinx.coroutines.delay
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds
import javax.inject.Inject

class GNewsRepository @Inject constructor(
    private val api: GNewsApi,
    @param:GNewsApiKey
    private val apiKey: String
) : NewsRepository {

    override suspend fun getDailyNews(
        from: Instant,
        to: Instant
    ): DailyNews {
        val domesticResponse = api.getTopHeadlines(
            apiKey = apiKey,
            category = BUSINESS_CATEGORY,
            language = JAPANESE_LANGUAGE,
            country = JAPAN_COUNTRY,
            maxArticles = FETCH_ARTICLE_COUNT,
            from = from.toString(),
            to = to.toString()
        )

        delay(freePlanRequestInterval)

        val worldResponse = api.getTopHeadlines(
            apiKey = apiKey,
            category = BUSINESS_CATEGORY,
            language = ENGLISH_LANGUAGE,
            country = null,
            maxArticles = FETCH_ARTICLE_COUNT,
            from = from.toString(),
            to = to.toString()
        )

        val domesticArticles =
            domesticResponse.articles.map { articleDto ->
                articleDto.toNewsArticle(NewsCategory.BUSINESS)
            }
        val prioritizedDomesticArticles =
            NewsArticleSelector.prioritizeByKeywords(
                articles = domesticArticles,
                keywords = DOMESTIC_ECONOMY_KEYWORDS
            )
        val worldArticles =
            worldResponse.articles.map { articleDto ->
                articleDto.toNewsArticle(NewsCategory.BUSINESS)
            }

        return DailyNews(
            domesticArticles = NewsArticleSelector.selectDiverse(
                articles = prioritizedDomesticArticles,
                maxArticles = DISPLAY_ARTICLE_COUNT
            ),
            worldArticles = NewsArticleSelector.selectDiverse(
                articles = worldArticles,
                maxArticles = DISPLAY_ARTICLE_COUNT
            )
        )
    }

    private companion object {
        const val BUSINESS_CATEGORY = "business"
        const val JAPANESE_LANGUAGE = "ja"
        const val ENGLISH_LANGUAGE = "en"
        const val JAPAN_COUNTRY = "jp"
        const val FETCH_ARTICLE_COUNT = 10
        const val DISPLAY_ARTICLE_COUNT = 5
        val freePlanRequestInterval = 1_100.milliseconds

        val DOMESTIC_ECONOMY_KEYWORDS = listOf(
            "政府",
            "日銀",
            "経済",
            "景気",
            "物価",
            "金利",
            "金融",
            "為替",
            "円相場",
            "賃金",
            "雇用",
            "gdp",
            "貿易",
            "輸出",
            "輸入",
            "予算",
            "税",
            "市場",
            "産業",
            "投資"
        )
    }
}
