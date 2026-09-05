package com.example.newsreader.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newsreader.R
import com.example.newsreader.ui.components.FeaturedNewsArticleItem
import com.example.newsreader.ui.components.NewsArticleItem
import com.example.newsreader.ui.components.NewsCategoryTabs
import com.example.newsreader.ui.components.NewsHeader
import com.example.newsreader.ui.components.NewsSection
import com.example.newsreader.ui.theme.NewsReaderTheme

@Composable
fun NewsReaderApp(
    modifier: Modifier = Modifier
) {
    val systemDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by rememberSaveable {
        mutableStateOf(systemDarkTheme)
    }

    NewsReaderTheme(darkTheme = isDarkTheme) {
        NewsReaderContent(
            isDarkTheme = isDarkTheme,
            onThemeToggle = {
                isDarkTheme = !isDarkTheme
            },
            modifier = modifier
        )
    }
}

@Composable
private fun NewsReaderContent(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by rememberSaveable {
        mutableStateOf(NewsSection.DOMESTIC)
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
                    dateText = "SUN, AUG 30",
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle
                )
            }

            item {
                NewsCategoryTabs(
                    selectedSection = selectedSection,
                    onSectionSelected = {
                        selectedSection = it
                    }
                )
            }

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
                            3
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            item {
                FeaturedNewsArticleItem(
                    articleNumber = 1,
                    category = "POLITICS",
                    publishedTime = "12 MIN AGO",
                    title = "政府、秋の経済対策について基本方針を発表",
                    description = "物価高への対応と成長分野への投資を柱に。",
                    source = "共同通信",
                    isRead = false,
                    onClick = {}
                )
            }

            item {
                NewsArticleItem(
                    articleNumber = 2,
                    category = "ECONOMY",
                    publishedTime = "08:15",
                    title = "企業の設備投資、3期連続で増加",
                    source = "日本経済新聞",
                    isRead = false,
                    onClick = {}
                )
            }

            item {
                NewsArticleItem(
                    articleNumber = 3,
                    category = "WEATHER",
                    publishedTime = "07:50",
                    title = "全国的に残暑、午後は急な雨に注意",
                    source = "NHK NEWS",
                    isRead = true,
                    onClick = {}
                )
            }

            item {
                NewsArticleItem(
                    articleNumber = 4,
                    category = "WORLD",
                    publishedTime = "07:30",
                    title = "各国首脳、共同声明案を協議",
                    source = "Reuters",
                    isRead = false,
                    onClick = {}
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun NewsReaderAppLightPreview() {
    NewsReaderTheme(darkTheme = false, dynamicColor = false) {
        NewsReaderContent(
            isDarkTheme = false,
            onThemeToggle = {}
        )
    }
}

@Preview(name = "Dark", showBackground = true)
@Composable
private fun NewsReaderAppDarkPreview() {
    NewsReaderTheme(darkTheme = true, dynamicColor = false) {
        NewsReaderContent(
            isDarkTheme = true,
            onThemeToggle = {}
        )
    }
}
