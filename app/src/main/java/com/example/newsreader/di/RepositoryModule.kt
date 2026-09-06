package com.example.newsreader.di

import com.example.newsreader.data.repository.GNewsRepository
import com.example.newsreader.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        repository: GNewsRepository
    ): NewsRepository
}
