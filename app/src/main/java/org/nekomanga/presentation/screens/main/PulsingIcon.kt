package org.nekomanga.presentation.screens.main

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun PulsingIcon(
    isPulsing: Boolean,
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Icon(imageVector = imageVector, contentDescription = contentDescription, modifier = modifier)
}
