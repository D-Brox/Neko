package org.nekomanga.presentation.components.theme

import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ThemeColorState(
    val primaryColor: Color,
    val textSelectionColors: TextSelectionColors,
    val containerColor: Color,
    val onContainerColor: Color,
    val altContainerColor: Color,
    val onAltContainerColor: Color,
)

@Composable
fun defaultThemeColorState(): ThemeColorState {
    return ThemeColorState(
        primaryColor = MaterialTheme.colorScheme.primary,
        textSelectionColors = LocalTextSelectionColors.current,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
        altContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        onAltContainerColor = MaterialTheme.colorScheme.onSurface,
    )
}
