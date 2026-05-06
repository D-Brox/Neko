package org.nekomanga.presentation.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.stringResource
import com.mudita.mmd.components.text.TextMMD
import jp.wasabeef.gap.Gap
import org.nekomanga.R
import org.nekomanga.presentation.theme.Size

/** Simple Dialog to add a new category */
@Composable
fun ClearDownloadQueueDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {

    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
        AlertDialog(
            text = {
                Column {
                    TextMMD(
                        text = stringResource(R.string.clear_download_queue),
                        style =
                            MaterialTheme.typography.bodyLarge.copy(
                                MaterialTheme.colorScheme.onSurface
                            ),
                    )
                    Gap(Size.medium)
                    TextMMD(
                        text = stringResource(R.string.clear_download_queue_confirmation),
                        style =
                            MaterialTheme.typography.bodyLarge.copy(
                                MaterialTheme.colorScheme.onSurface
                            ),
                    )
                }
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    colors =
                        ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                ) {
                    TextMMD(text = stringResource(id = R.string.clear))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    colors =
                        ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                ) {
                    TextMMD(text = stringResource(id = R.string.cancel))
                }
            },
        )
    }
}
