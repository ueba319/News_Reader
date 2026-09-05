package com.example.newsreader.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newsreader.R
import com.example.newsreader.ui.theme.NewsReaderTheme

enum class NewsSection {
    DOMESTIC,
    WORLD
}

@Composable
fun NewsCategoryTabs(
    selectedSection: NewsSection,
    onSectionSelected: (NewsSection) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(4.dp)
            .selectableGroup()
    ) {
        NewsCategoryTab(
            text = stringResource(R.string.news_section_domestic),
            selected = selectedSection == NewsSection.DOMESTIC,
            onClick = {
                onSectionSelected(NewsSection.DOMESTIC)
            },
            modifier = Modifier.weight(1f)
        )

        NewsCategoryTab(
            text = stringResource(R.string.news_section_world),
            selected = selectedSection == NewsSection.WORLD,
            onClick = {
                onSectionSelected(NewsSection.WORLD)
            },
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
private fun NewsCategoryTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (selected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.Tab
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    }
}


@Preview(name = "News Category Tabs - Dark", showBackground = true)
@Composable
private fun NewsCategoryTabsPreview() {
    NewsReaderTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            var selectedSection by remember {
                mutableStateOf(NewsSection.DOMESTIC)
            }

            NewsCategoryTabs(
                selectedSection = selectedSection,
                onSectionSelected = {
                    selectedSection = it
                },
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}





