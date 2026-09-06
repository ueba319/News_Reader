package com.example.newsreader.ui.news

import com.example.newsreader.domain.model.DailyNews

sealed interface NewsUiState {

    data object Loading : NewsUiState

    data class Success(
        val dailyNews: DailyNews
    ) : NewsUiState

    data object Empty : NewsUiState

    data object Error : NewsUiState
}