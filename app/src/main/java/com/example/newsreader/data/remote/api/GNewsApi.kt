package com.example.newsreader.data.remote.api

import com.example.newsreader.data.remote.dto.GNewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GNewsApi {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Header("X-Api-Key")
        apiKey: String,

        @Query("category")
        category: String,

        @Query("lang")
        language: String = "ja",

        @Query("country")
        country: String? = null,

        @Query("max")
        maxArticles: Int = 5,

        @Query("from")
        from: String? = null,

        @Query("to")
        to: String? = null
    ): GNewsResponseDto
}
