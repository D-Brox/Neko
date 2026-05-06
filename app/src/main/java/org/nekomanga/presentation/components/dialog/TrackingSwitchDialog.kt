package org.nekomanga.presentation.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mudita.mmd.components.text.TextMMD
import jp.wasabeef.gap.Gap
import org.nekomanga.R
import org.nekomanga.presentation.components.theme.ThemeColorState
import org.nekomanga.presentation.theme.Size

@Composable
fun TrackingSwitchDialog(
    themeColorState: ThemeColorState,
    name: String,
    oldName: String,
    newName: String,
    onConfirm: (Boolean, Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = { TextMMD(text = stringResource(id = R.string.remove_previous_tracker)) },
        text = {
            Column {
                val isReplacing = oldName != newName
                TextButton(
                    onClick = { onConfirm(true, isReplacing) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextMMD(
                        text =
                            if (isReplacing) {
                                stringResource(
                                    id = R.string.remove_x_from_service_and_add_y,
                                    oldName,
                                    newName,
                                )
                            } else {
                                stringResource(
                                    id = R.string.remove_x_from_service,
                                    oldName,
                                    newName,
                                )
                            }
                    )
                }
                Gap(Size.small)
                TextButton(
                    onClick = { onConfirm(false, isReplacing) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextMMD(
                        text =
                            if (isReplacing) {
                                stringResource(id = R.string.keep_both_on_service, newName)
                            } else {
                                stringResource(id = R.string.keep_on_service, newName)
                            }
                    )
                }
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors =
                    ButtonDefaults.textButtonColors(contentColor = themeColorState.primaryColor),
            ) {
                TextMMD(text = stringResource(id = R.string.cancel))
            }
        },
    )
}
