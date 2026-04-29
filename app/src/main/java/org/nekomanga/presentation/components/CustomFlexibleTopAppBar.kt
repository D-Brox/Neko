package org.nekomanga.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color

/**
 * A static top bar that is always fully visible and doesn't collapse on scroll. Uses a Box with
 * background draw, then applies statusBarsPadding to the inner Column so the background fills the
 * status bar area while content is properly inset.
 */
@Composable
fun FlexibleTopBar(
    modifier: Modifier = Modifier,
    colors: FlexibleTopBarColors = FlexibleTopBarDefaults.topAppBarColors(),
    content: @Composable () -> Unit,
) {
    val color = colors.containerColor()
    Box(modifier = modifier.fillMaxWidth().drawBehind { drawRect(color) }) {
        Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) { content() }
    }
}

@Stable
class FlexibleTopBarColors internal constructor(val containerColor: Color) {

    @Composable
    fun containerColor(): Color {
        return containerColor
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is FlexibleTopBarColors) return false
        return containerColor == other.containerColor
    }

    override fun hashCode(): Int = containerColor.hashCode()
}

object FlexibleTopBarDefaults {

    @Composable
    fun topAppBarColors(
        containerColor: Color = MaterialTheme.colorScheme.primary
    ): FlexibleTopBarColors = FlexibleTopBarColors(containerColor)
}
