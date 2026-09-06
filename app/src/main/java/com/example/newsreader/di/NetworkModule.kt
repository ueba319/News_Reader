package com.example.newsreader.di

import com.example.newsreader.BuildConfig
import com.example.newsreader.data.remote.api.GNewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://gnews.io/api/v4/"

    @Provides
    @Singleton
    fun provideGNewsApi(): GNewsApi {
        val json = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
            .create(GNewsApi::class.java)
    }

    @Provides
    @GNewsApiKey
    fun provideGNewsApiKey(): String = BuildConfig.GNEWS_API_KEY

    @Provides
    @Singleton
    fun provideClock(): Clock = Clock.systemUTC()
}
