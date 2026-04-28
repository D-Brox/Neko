package org.nekomanga.presentation.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.mudita.mmd.components.text.TextMMD
import org.nekomanga.R
import org.nekomanga.presentation.theme.Size

/** Dialog for logout confirmation */
@Composable
fun LogoutDialog(sourceName: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Size.tiny),
        title = { TextMMD(text = stringResource(id = R.string.sign_out_from_, sourceName)) },
        text = {},
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                TextMMD(text = stringResource(id = R.string.sign_out))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { TextMMD(text = stringResource(id = R.string.cancel)) }
        },
    )
}
