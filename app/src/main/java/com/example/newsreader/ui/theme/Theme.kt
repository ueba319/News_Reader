package com.example.newsreader.ui.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext


private val LightColorScheme = lightColorScheme(
    primary = NewsLightPrimary,
    onPrimary = NewsLightOnPrimary,
    primaryContainer = NewsLightPrimaryContainer,
    onPrimaryContainer = NewsLightOnPrimaryContainer,
    secondary = NewsLightSecondary,
    onSecondary = NewsLightOnSecondary,
    background = NewsLightBackground,
    onBackground = NewsLightOnBackground,
    surface = NewsLightSurface,
    onSurface = NewsLightOnSurface,
    surfaceVariant = NewsLightSurfaceVariant,
    onSurfaceVariant = NewsLightOnSurfaceVariant,
    outline = NewsLightOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = NewsPrimary,
    onPrimary = NewsBackground,
    primaryContainer = NewsPrimaryContainer,
    onPrimaryContainer = NewsOnPrimaryContainer,
    background = NewsBackground,
    onBackground = NewsOnBackground,
    surface = NewsSurface,
    onSurface = NewsOnSurface,
    surfaceVariant = NewsSurfaceVariant,
    onSurfaceVariant = NewsOnSurfaceVariant,
    outline = NewsOutline
)

@Composable
fun NewsReaderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val themeProgress by animateFloatAsState(
        targetValue = if (darkTheme) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "Theme color transition"
    )

    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> lerpColorScheme(
            start = LightColorScheme,
            stop = DarkColorScheme,
            fraction = themeProgress
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private fun lerpColorScheme(
    start: ColorScheme,
    stop: ColorScheme,
    fraction: Float
): ColorScheme = start.copy(
    primary = lerp(start.primary, stop.primary, fraction),
    onPrimary = lerp(start.onPrimary, stop.onPrimary, fraction),
    primaryContainer = lerp(start.primaryContainer, stop.primaryContainer, fraction),
    onPrimaryContainer = lerp(start.onPrimaryContainer, stop.onPrimaryContainer, fraction),
    secondary = lerp(start.secondary, stop.secondary, fraction),
    onSecondary = lerp(start.onSecondary, stop.onSecondary, fraction),
    background = lerp(start.background, stop.background, fraction),
    onBackground = lerp(start.onBackground, stop.onBackground, fraction),
    surface = lerp(start.surface, stop.surface, fraction),
    onSurface = lerp(start.onSurface, stop.onSurface, fraction),
    surfaceVariant = lerp(start.surfaceVariant, stop.surfaceVariant, fraction),
    onSurfaceVariant = lerp(start.onSurfaceVariant, stop.onSurfaceVariant, fraction),
    outline = lerp(start.outline, stop.outline, fraction)
)
