package com.example.newsreader.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsreader.R
import com.example.newsreader.ui.components.FeaturedNewsArticleItem
import com.example.newsreader.ui.components.NewsArticleItem
import com.example.newsreader.ui.components.NewsCategoryTabs
import com.example.newsreader.ui.components.NewsHeader
import com.example.newsreader.ui.components.NewsSection
import com.example.newsreader.ui.news.NewsUiState
import com.example.newsreader.ui.news.NewsViewModel
import com.example.newsreader.ui.theme.NewsReaderTheme
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun NewsReaderApp(
    modifier: Modifier = Modifier
) {
    val viewModel: NewsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    val systemDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by rememberSaveable {
        mutableStateOf(systemDarkTheme)
    }

    NewsReaderTheme(darkTheme = isDarkTheme) {
        NewsReaderContent(
            uiState = uiState,
            isDarkTheme = isDarkTheme,
            onThemeToggle = {
                isDarkTheme = !isDarkTheme
            },
            onRetry = viewModel::loadNews,
            onArticleClick = { url ->
                uriHandler.openUri(url)
            },
            modifier = modifier
        )
    }
}

@Composable
private fun NewsReaderContent(
    uiState: NewsUiState,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onRetry: () -> Unit,
    onArticleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by rememberSaveable {
        mutableStateOf(NewsSection.DOMESTIC)
    }
    val selectedArticles = when (uiState) {
        is NewsUiState.Success -> {
            when (selectedSection) {
                NewsSection.DOMESTIC ->
                    uiState.dailyNews.domesticArticles

                NewsSection.WORLD ->
                    uiState.dailyNews.worldArticles
            }
        }

        else -> emptyList()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                NewsHeader(
                    dateText = headerDateFormatter.format(LocalDate.now()),
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle
                )
            }

            item {
                NewsCategoryTabs(
                    selectedSection = selectedSection,
                    onSectionSelected = { selectedSection = it }
                )
            }

            when (uiState) {
                NewsUiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                NewsUiState.Empty -> {
                    item {
                        Text(
                            text = stringResource(R.string.news_empty),
                            modifier = Modifier.padding(vertical = 48.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                NewsUiState.Error -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.news_load_error),
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Button(onClick = onRetry) {
                                Text(text = stringResource(R.string.retry))
                            }
                        }
                    }
                }

                is NewsUiState.Success -> {
                    if (selectedArticles.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.news_empty),
                                modifier = Modifier.padding(vertical = 48.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.top_stories),
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = stringResource(
                                        R.string.remaining_articles,
                                        selectedArticles.size
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        val featuredArticle = selectedArticles.first()

                        item(key = featuredArticle.id) {
                            FeaturedNewsArticleItem(
                                articleNumber = 1,
                                category = featuredArticle.category.name,
                                publishedTime = articleTimeFormatter.format(
                                    featuredArticle.publishedAt
                                ),
                                title = featuredArticle.title,
                                description = featuredArticle.description.orEmpty(),
                                source = featuredArticle.sourceName,
                                isRead = false,
                                onClick = {
                                    onArticleClick(featuredArticle.url)
                                }
                            )
                        }

                        itemsIndexed(
                            items = selectedArticles.drop(1),
                            key = { _, article -> article.id }
                        ) { index, article ->
                            NewsArticleItem(
                                articleNumber = index + 2,
                                category = article.category.name,
                                publishedTime = articleTimeFormatter.format(
                                    article.publishedAt
                                ),
                                title = article.title,
                                source = article.sourceName,
                                isRead = false,
                                onClick = {
                                    onArticleClick(article.url)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private val headerDateFormatter: DateTimeFormatter =
    DateTimeFormatter
        .ofPattern("EEE, MMM d", Locale.ENGLISH)

private val articleTimeFormatter: DateTimeFormatter =
    DateTimeFormatter
        .ofPattern("HH:mm")
        .withZone(ZoneId.systemDefault())

@Preview(name = "Light", showBackground = true)
@Composable
private fun NewsReaderAppLightPreview() {
    NewsReaderTheme(darkTheme = false, dynamicColor = false) {
        NewsReaderContent(
            isDarkTheme = false,
            onThemeToggle = {},
            uiState = NewsUiState.Loading,
            onRetry = {},
            onArticleClick = {},
        )
    }
}

@Preview(name = "Dark", showBackground = true)
@Composable
private fun NewsReaderAppDarkPreview() {
    NewsReaderTheme(darkTheme = true, dynamicColor = false) {
        NewsReaderContent(
            isDarkTheme = true,
            onThemeToggle = {},
            uiState = NewsUiState.Loading,
            onRetry = {},
            onArticleClick = {},
        )
    }
}
