package org.nekomanga.presentation.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mudita.mmd.components.text.TextMMD
import org.nekomanga.R

@Composable
fun ConfirmationDialog(
    title: String,
    body: String? = null,
    confirmButton: String = stringResource(R.string.ok),
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        title = { TextMMD(text = title) },
        text = {
            if (body != null) {
                TextMMD(text = body)
            } else null
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                TextMMD(text = confirmButton)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { TextMMD(text = stringResource(id = R.string.cancel)) }
        },
    )
}
