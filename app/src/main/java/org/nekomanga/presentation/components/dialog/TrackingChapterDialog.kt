package org.nekomanga.presentation.components.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mudita.mmd.components.text.TextMMD
import org.nekomanga.R
import org.nekomanga.domain.track.TrackItem
import org.nekomanga.presentation.components.NekoPicker
import org.nekomanga.presentation.components.theme.ThemeColorState
import org.nekomanga.presentation.theme.Size

@Composable
fun TrackingChapterDialog(
    themeColorState: ThemeColorState,
    track: TrackItem,
    onDismiss: () -> Unit,
    trackChapterChanged: (Int) -> Unit,
) {
    var currentChapter by remember { mutableStateOf(track.lastChapterRead.toInt()) }

    val range =
        when (track.totalChapters > 0) {
            true -> track.totalChapters
            false -> 10000
        }

    AlertDialog(
        title = {
            TextMMD(
                text = stringResource(id = R.string.chapters),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        text = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(Size.medium).fillMaxWidth(),
            ) {
                NekoPicker(
                    value = currentChapter,
                    modifier = Modifier.fillMaxWidth(.4f),
                    items = (0..range).toList(),
                    onValueChange = { currentChapter = it },
                    themeColorState = themeColorState,
                )
            }
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors =
                    ButtonDefaults.textButtonColors(contentColor = themeColorState.primaryColor),
            ) {
                TextMMD(text = stringResource(id = R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    trackChapterChanged(currentChapter)
                    onDismiss()
                },
                colors =
                    ButtonDefaults.textButtonColors(contentColor = themeColorState.primaryColor),
            ) {
                TextMMD(text = stringResource(id = android.R.string.ok))
            }
        },
    )
}
