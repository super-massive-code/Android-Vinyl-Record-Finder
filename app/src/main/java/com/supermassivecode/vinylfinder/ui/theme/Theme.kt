package com.supermassivecode.vinylfinder.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val standardPadding = 8.dp

private val DarkColorPalette = darkColors(
    primary = BlueGrey900,
    onPrimary = BlueGrey50,
    primaryVariant = BlueGrey300,
    secondary = BlueGrey900,
    background = BlueGrey700,
//    surface = BlueGrey900,
//    onSurface = BlueGrey50,
    )

//private val LightColorPalette = lightColors(
//        primary = Purple500,
//        primaryVariant = Purple700,
//        secondary = Teal200

/* Other default colors to override
background = Color.White,
surface = Color.White,
onPrimary = Color.White,
onSecondary = Color.Black,
onBackground = Color.Black,
onSurface = Color.Black,
*/


@Composable
fun VinylFinderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}