package com.example.newsreader.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.newsreader.R
import com.example.newsreader.ui.theme.NewsReaderTheme

@Composable
fun NewsReaderApp(
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun NewsReaderAppLightPreview() {
    NewsReaderTheme(darkTheme = false, dynamicColor = false) { NewsReaderApp() }
}

@Preview(name = "Dark", showBackground = true)
@Composable
private fun NewsReaderAppDarkPreview() {
    NewsReaderTheme(darkTheme = true, dynamicColor = false) { NewsReaderApp() }
}