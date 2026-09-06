package com.example.newsreader.di

import com.example.newsreader.data.repository.FakeNewsRepository
import com.example.newsreader.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class FakeRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        repository: FakeNewsRepository
    ): NewsRepository
}
