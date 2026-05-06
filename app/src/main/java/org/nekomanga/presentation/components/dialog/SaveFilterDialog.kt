package org.nekomanga.presentation.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mudita.mmd.components.text.TextMMD
import eu.kanade.tachiyomi.data.database.models.BrowseFilterImpl
import jp.wasabeef.gap.Gap
import org.nekomanga.R
import org.nekomanga.presentation.components.theme.ThemeColorState
import org.nekomanga.presentation.theme.Size

/** Simple Dialog to save a filter */
@Composable
fun SaveFilterDialog(
    themeColorState: ThemeColorState,
    currentSavedFilters: List<BrowseFilterImpl>,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var saveFilterText by remember { mutableStateOf("") }
    var saveEnabled by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(saveFilterText, currentSavedFilters) {
        if (saveFilterText.isEmpty()) {
            saveEnabled = false
            errorMessage = ""
        } else if (currentSavedFilters.any { it.name.equals(saveFilterText, true) }) {
            saveEnabled = false
            errorMessage = context.getString(R.string.filter_with_name_exists)
        } else {
            saveEnabled = true
            errorMessage = ""
        }
    }

    AlertDialog(
        title = { TextMMD(text = stringResource(id = R.string.save_filter)) },
        text = {
            Column {
                OutlinedTextField(
                    value = saveFilterText,
                    onValueChange = { saveFilterText = it },
                    label = { TextMMD(text = stringResource(id = R.string.name)) },
                    singleLine = true,
                    maxLines = 1,
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            cursorColor = themeColorState.primaryColor,
                            focusedLabelColor = themeColorState.primaryColor,
                            focusedBorderColor = themeColorState.primaryColor,
                        ),
                )
                Gap(Size.extraTiny)
                TextMMD(
                    text = errorMessage,
                    style =
                        MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.error
                        ),
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(saveFilterText)
                    onDismiss()
                },
                enabled = saveEnabled,
                colors =
                    ButtonDefaults.textButtonColors(contentColor = themeColorState.primaryColor),
            ) {
                TextMMD(text = stringResource(id = R.string.save))
            }
        },
        dismissButton = {
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
