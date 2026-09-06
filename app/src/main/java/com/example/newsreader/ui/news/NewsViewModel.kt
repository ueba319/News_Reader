package com.example.newsreader.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsreader.domain.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.CancellationException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val clock: Clock = Clock.systemUTC()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<NewsUiState>(NewsUiState.Loading)

    val uiState: StateFlow<NewsUiState> =
        _uiState.asStateFlow()

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading

            try {
                val to = Instant.now(clock)
                val from = to.minus(
                    24,
                    ChronoUnit.HOURS
                )

                val dailyNews = repository.getDailyNews(
                    from = from,
                    to = to
                )

                val hasNoArticles =
                    dailyNews.domesticArticles.isEmpty() &&
                            dailyNews.worldArticles.isEmpty()

                _uiState.value = if (hasNoArticles) {
                    NewsUiState.Empty
                } else {
                    NewsUiState.Success(dailyNews)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = NewsUiState.Error
            }
        }
    }
}
