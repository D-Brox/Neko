package org.nekomanga.presentation.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mudita.mmd.components.buttons.ButtonMMD
import com.mudita.mmd.components.text.TextMMD
import org.nekomanga.BuildConfig
import org.nekomanga.R

@Composable
fun WhatsNewDialog(onDismissRequest: () -> Unit, onSeeWhatsNewClick: () -> Unit) {
    AlertDialog(
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextMMD(text = stringResource(id = R.string.updated_to_, BuildConfig.VERSION_NAME))
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ButtonMMD(onClick = onSeeWhatsNewClick) {
                    TextMMD(text = stringResource(id = R.string.whats_new_this_release))
                }
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                TextMMD(text = stringResource(id = R.string.close))
            }
        },
    )
}
