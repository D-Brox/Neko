package org.nekomanga.presentation.screens.settings.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mudita.mmd.components.text.TextMMD
import org.nekomanga.presentation.theme.Size

@Composable
fun PreferenceGroupHeader(title: String) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier.fillMaxWidth().padding(bottom = Size.small, top = Size.medium),
    ) {
        TextMMD(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = Size.medium),
            style = MaterialTheme.typography.titleSmall,
        )
    }
}
