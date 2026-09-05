package com.example.newsreader.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newsreader.R
import com.example.newsreader.ui.theme.NewsReaderTheme
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun NewsHeader(
    dateText: String,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val toggleDescription = stringResource(
        if (isDarkTheme) {
            R.string.theme_switch_to_light
        } else {
            R.string.theme_switch_to_dark
        }
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dateText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.news_header_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Surface(
            onClick = onThemeToggle,
            modifier = Modifier
                .size(48.dp)
                .semantics {
                    contentDescription = toggleDescription
                },
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(contentAlignment = Alignment.Center) {
                ThemeToggleIcon(
                    showSun = isDarkTheme,
                    color = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun ThemeToggleIcon(
    showSun: Boolean,
    color: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val iconCenter = center
        val iconSize = size.minDimension

        if (showSun) {
            drawCircle(
                color = color,
                radius = iconSize * 0.2f,
                center = iconCenter
            )

            repeat(8) { index ->
                val angle = index * Math.PI / 4
                val directionX = cos(angle).toFloat()
                val directionY = sin(angle).toFloat()
                val rayStart = iconSize * 0.34f
                val rayEnd = iconSize * 0.48f

                drawLine(
                    color = color,
                    start = Offset(
                        x = iconCenter.x + directionX * rayStart,
                        y = iconCenter.y + directionY * rayStart
                    ),
                    end = Offset(
                        x = iconCenter.x + directionX * rayEnd,
                        y = iconCenter.y + directionY * rayEnd
                    ),
                    strokeWidth = 1.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        } else {
            val moonRadius = iconSize * 0.36f

            drawCircle(
                color = color,
                radius = moonRadius,
                center = iconCenter
            )
            drawCircle(
                color = backgroundColor,
                radius = moonRadius,
                center = Offset(
                    x = iconCenter.x + iconSize * 0.18f,
                    y = iconCenter.y - iconSize * 0.1f
                )
            )
        }
    }
}

@Preview(name = "News Header - Dark", showBackground = true)
@Composable
private fun NewsHeaderPreview() {
    NewsReaderTheme(darkTheme = true, dynamicColor = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            NewsHeader(
                dateText = "SUN, AUG 30",
                isDarkTheme = true,
                onThemeToggle = {},
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}
