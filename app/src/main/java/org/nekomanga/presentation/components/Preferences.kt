package org.nekomanga.presentation.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mudita.mmd.components.divider.HorizontalDividerMMD

@Composable
fun Divider(modifier: Modifier = Modifier) {
    HorizontalDividerMMD(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        modifier = modifier,
    )
}

@Composable
fun VerticalDivider(modifier: Modifier = Modifier) {
    HorizontalDividerMMD(
        modifier = Modifier.fillMaxHeight().width(1.dp).then(modifier),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
    )
}
