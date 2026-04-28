package org.nekomanga.presentation.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mudita.mmd.components.text.TextMMD
import org.nekomanga.R

@Composable
fun DataSaverDialog(onDismissRequest: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextMMD(text = stringResource(id = R.string.data_saver_warning))
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextMMD(text = stringResource(id = R.string.data_saver_warning_summary))
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                TextMMD(text = stringResource(id = R.string.data_saver_warning_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                TextMMD(text = stringResource(id = R.string.cancel))
            }
        },
    )
}
