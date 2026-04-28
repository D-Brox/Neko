package org.nekomanga.presentation.components.dialog

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mudita.mmd.components.text.TextMMD
import eu.kanade.tachiyomi.data.backup.BackupFileValidator
import jp.wasabeef.gap.Gap
import org.nekomanga.R
import org.nekomanga.presentation.theme.Size

/** Dialog for restoring backup */
@Composable
fun RestoreDialog(uri: Uri, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    val context = LocalContext.current
    val results = BackupFileValidator().validate(context, uri)

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Size.tiny),
        title = { TextMMD(text = stringResource(id = R.string.restore_backup)) },
        text = {
            Column {
                TextMMD(text = stringResource(id = R.string.restore_neko))
                if (results.missingMangaDexEntries) {
                    Gap(Size.small)
                    TextMMD(text = stringResource(id = R.string.restore_missing_mangadex))
                }
                if (results.missingTrackers.isNotEmpty()) {
                    Gap(Size.small)
                    TextMMD(text = stringResource(id = R.string.restore_missing_trackers))
                    results.missingTrackers.forEach { tracker ->
                        Gap(Size.tiny)
                        TextMMD(text = "- $tracker")
                    }
                }

                Gap(Size.tiny)
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                TextMMD(text = stringResource(id = R.string.restore))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { TextMMD(text = stringResource(id = R.string.cancel)) }
        },
    )
}
