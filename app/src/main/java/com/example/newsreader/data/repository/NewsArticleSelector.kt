package com.example.newsreader.data.repository

import com.example.newsreader.domain.model.NewsArticle

internal object NewsArticleSelector {

    fun prioritizeByKeywords(
        articles: List<NewsArticle>,
        keywords: List<String>
    ): List<NewsArticle> {
        val normalizedKeywords = keywords.map { it.lowercase() }
        val (matchingArticles, otherArticles) =
            articles.partition { article ->
                val searchableText = buildString {
                    append(article.title)
                    append(' ')
                    append(article.description.orEmpty())
                }.lowercase()

                normalizedKeywords.any(searchableText::contains)
            }

        return matchingArticles + otherArticles
    }

    fun selectDiverse(
        articles: List<NewsArticle>,
        maxArticles: Int
    ): List<NewsArticle> {
        return selectBalanced(
            primaryArticles = articles,
            secondaryArticles = emptyList(),
            maxArticles = maxArticles,
            primaryQuota = maxArticles
        )
    }

    fun selectBalanced(
        primaryArticles: List<NewsArticle>,
        secondaryArticles: List<NewsArticle>,
        maxArticles: Int,
        primaryQuota: Int
    ): List<NewsArticle> {
        require(maxArticles >= 0)
        require(primaryQuota in 0..maxArticles)

        val selectedArticles = mutableListOf<NewsArticle>()
        val selectedIds = mutableSetOf<String>()
        val selectedUrls = mutableSetOf<String>()
        val selectedSources = mutableSetOf<String>()

        fun addArticle(
            article: NewsArticle,
            requireNewSource: Boolean
        ): Boolean {
            if (article.id in selectedIds || article.url in selectedUrls) {
                return false
            }

            val normalizedSource = article.sourceName.trim().lowercase()
            if (requireNewSource && normalizedSource in selectedSources) {
                return false
            }

            selectedArticles += article
            selectedIds += article.id
            selectedUrls += article.url
            selectedSources += normalizedSource
            return true
        }

        fun addFrom(
            candidates: List<NewsArticle>,
            count: Int,
            requireNewSource: Boolean
        ) {
            var addedCount = 0
            for (article in candidates) {
                if (selectedArticles.size >= maxArticles || addedCount >= count) {
                    break
                }
                if (addArticle(article, requireNewSource)) {
                    addedCount++
                }
            }
        }

        addFrom(
            candidates = primaryArticles,
            count = primaryQuota,
            requireNewSource = true
        )
        addFrom(
            candidates = secondaryArticles,
            count = maxArticles - primaryQuota,
            requireNewSource = true
        )

        val remainingCandidates = buildList {
            val largestSize = maxOf(
                primaryArticles.size,
                secondaryArticles.size
            )
            repeat(largestSize) { index ->
                primaryArticles.getOrNull(index)?.let(::add)
                secondaryArticles.getOrNull(index)?.let(::add)
            }
        }

        addFrom(
            candidates = remainingCandidates,
            count = maxArticles - selectedArticles.size,
            requireNewSource = true
        )
        addFrom(
            candidates = remainingCandidates,
            count = maxArticles - selectedArticles.size,
            requireNewSource = false
        )

        return selectedArticles
    }
}
