package com.example.newsreader.domain.repository

import com.example.newsreader.domain.model.DailyNews
import java.time.Instant

interface NewsRepository {

    suspend fun getDailyNews(
        from: Instant,
        to: Instant
    ): DailyNews
}