package org.nekomanga.presentation.theme.colorschemes

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Monochrome color scheme optimized for E-ink displays
object MMDColorScheme {
    private val Black = Color(0xFF000000)
    private val White = Color(0xFFFFFFFF)
    private val Gray = Color(0xFF808080)
    private val LightGray = Color(0xFFC0C0C0)
    private val DarkGray = Color(0xFF404040)

    val darkScheme: ColorScheme =
        darkColorScheme(
            primary = White,
            onPrimary = Black,
            primaryContainer = DarkGray,
            onPrimaryContainer = White,
            secondary = LightGray,
            onSecondary = Black,
            secondaryContainer = DarkGray,
            onSecondaryContainer = White,
            tertiary = LightGray,
            onTertiary = Black,
            background = Black,
            onBackground = White,
            surface = Black,
            onSurface = White,
            surfaceVariant = DarkGray,
            onSurfaceVariant = LightGray,
            outline = Gray,
        )

    val lightScheme: ColorScheme =
        lightColorScheme(
            primary = Black,
            onPrimary = White,
            primaryContainer = LightGray,
            onPrimaryContainer = Black,
            secondary = DarkGray,
            onSecondary = White,
            secondaryContainer = LightGray,
            onSecondaryContainer = Black,
            tertiary = DarkGray,
            onTertiary = White,
            background = White,
            onBackground = Black,
            surface = White,
            onSurface = Black,
            surfaceVariant = LightGray,
            onSurfaceVariant = DarkGray,
            outline = Gray,
        )

    fun getColorScheme(isDark: Boolean): ColorScheme {
        return if (isDark) darkScheme else lightScheme
    }
}
