package com.supermassivecode.vinylfinder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val standardPadding = 8.dp

private val DarkColorScheme = darkColorScheme(
    primary = BlueGrey300,
    onPrimary = BlueGrey900,
    primaryContainer = BlueGrey900,
    onPrimaryContainer = BlueGrey50,
    secondary = BlueGrey900,
    background = BlueGrey700,
    surface = BlueGrey900,
    onSurface = BlueGrey50
)

@Composable
fun VinylFinderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
