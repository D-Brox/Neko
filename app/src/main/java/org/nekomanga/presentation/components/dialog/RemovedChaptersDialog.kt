package org.nekomanga.presentation.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mudita.mmd.components.text.TextMMD
import kotlinx.collections.immutable.PersistentList
import org.nekomanga.R
import org.nekomanga.domain.chapter.ChapterItem
import org.nekomanga.presentation.components.theme.ThemeColorState

@Composable
fun RemovedChaptersDialog(
    themeColorState: ThemeColorState,
    chapters: PersistentList<ChapterItem>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    AlertDialog(
        title = { TextMMD(text = stringResource(id = R.string.delete_removed_chapters)) },
        text = {
            val chapterNames = chapters.map { it.chapter.name }
            TextMMD(
                text =
                    context.resources.getQuantityString(
                        R.plurals.deleted_chapters,
                        chapters.size,
                        chapters.size,
                        if (chapters.size > 5) {
                            "${chapterNames.take(5).joinToString(", ")}, " +
                                context.resources.getQuantityString(
                                    R.plurals.notification_and_n_more,
                                    (chapters.size - (4 - 1)),
                                    (chapters.size - (4 - 1)),
                                )
                        } else {
                            chapterNames.joinToString(", ")
                        },
                    )
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                colors =
                    ButtonDefaults.textButtonColors(contentColor = themeColorState.primaryColor),
            ) {
                TextMMD(text = stringResource(id = R.string.delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors =
                    ButtonDefaults.textButtonColors(contentColor = themeColorState.primaryColor),
            ) {
                TextMMD(text = stringResource(id = R.string.keep))
            }
        },
    )
}
