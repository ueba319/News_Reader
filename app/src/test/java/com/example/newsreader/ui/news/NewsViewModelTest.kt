package com.example.newsreader.ui.news

import com.example.newsreader.MainDispatcherRule
import com.example.newsreader.domain.model.DailyNews
import com.example.newsreader.domain.model.NewsArticle
import com.example.newsreader.domain.model.NewsCategory
import com.example.newsreader.domain.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fixedInstant =
        Instant.parse("2026-09-06T07:00:00Z")
    private val fixedClock =
        Clock.fixed(fixedInstant, ZoneOffset.UTC)

    @Test
    fun initialization_setsSuccessAndRequestsPrevious24Hours() = runTest {
        val expectedNews = DailyNews(
            domesticArticles = listOf(article("domestic")),
            worldArticles = listOf(article("world"))
        )
        val repository = FakeNewsRepository(result = expectedNews)

        val viewModel = NewsViewModel(repository, fixedClock)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is NewsUiState.Success)
        assertEquals(
            expectedNews,
            (state as NewsUiState.Success).dailyNews
        )
        assertEquals(fixedInstant, repository.requestedTo)
        assertEquals(
            fixedInstant.minus(24, ChronoUnit.HOURS),
            repository.requestedFrom
        )
    }

    @Test
    fun initialization_setsEmptyWhenBothListsAreEmpty() = runTest {
        val repository = FakeNewsRepository(
            result = DailyNews(
                domesticArticles = emptyList(),
                worldArticles = emptyList()
            )
        )

        val viewModel = NewsViewModel(repository, fixedClock)
        advanceUntilIdle()

        assertSame(NewsUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun initialization_setsErrorWhenRepositoryFails() = runTest {
        val repository = FakeNewsRepository(
            failure = IllegalStateException("test failure")
        )

        val viewModel = NewsViewModel(repository, fixedClock)
        advanceUntilIdle()

        assertSame(NewsUiState.Error, viewModel.uiState.value)
    }

    private fun article(id: String): NewsArticle {
        return NewsArticle(
            id = id,
            title = "title-$id",
            description = "description-$id",
            url = "https://example.com/$id",
            publishedAt = fixedInstant,
            sourceName = "Example News",
            sourceUrl = "https://example.com",
            category = NewsCategory.BUSINESS
        )
    }

    private class FakeNewsRepository(
        private val result: DailyNews? = null,
        private val failure: Exception? = null
    ) : NewsRepository {

        var requestedFrom: Instant? = null
            private set
        var requestedTo: Instant? = null
            private set

        override suspend fun getDailyNews(
            from: Instant,
            to: Instant
        ): DailyNews {
            requestedFrom = from
            requestedTo = to
            failure?.let { throw it }
            return requireNotNull(result)
        }
    }
}
