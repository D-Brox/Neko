package org.nekomanga.presentation.screens.settings.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.mudita.mmd.components.buttons.ButtonMMD
import com.mudita.mmd.components.buttons.ButtonDefaultsMMD
import com.mudita.mmd.components.divider.HorizontalDividerMMD
import com.mudita.mmd.components.lazy.LazyColumnMMD
import com.mudita.mmd.components.text.TextMMD
import org.nekomanga.R
import org.nekomanga.presentation.theme.Size

@Composable
fun <T> ListPreferenceWidget(
    value: T,
    title: String,
    subtitle: String?,
    icon: ImageVector?,
    entries: Map<out T, String>,
    onValueChange: (T) -> Unit,
) {
    var isDialogShown by remember { mutableStateOf(false) }

    TextPreferenceWidget(
        title = title,
        subtitle = subtitle,
        icon = icon,
        onPreferenceClick = { isDialogShown = true },
    )

    if (isDialogShown) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Size.tiny),
            onDismissRequest = { isDialogShown = false },
            title = { TextMMD(text = title) },
            text = {
                Box {
                    val state = rememberLazyListState()
                    LazyColumnMMD(state = state) {
                        entries.forEach { current ->
                            val isSelected = value == current.key
                            item(key = current.key) {
                                DialogRow(
                                    label = current.value,
                                    isSelected = isSelected,
                                    onSelected = {
                                        onValueChange(current.key!!)
                                        isDialogShown = false
                                    },
                                )
                            }
                        }
                    }
                    if (state.canScrollBackward)
                        HorizontalDividerMMD(modifier = Modifier.align(Alignment.TopCenter))
                    if (state.canScrollForward)
                        HorizontalDividerMMD(modifier = Modifier.align(Alignment.BottomCenter))
                }
            },
            confirmButton = {
                TextButton(onClick = { isDialogShown = false }) {
                    TextMMD(text = stringResource(R.string.cancel))
                }
            },
        )
    }
}

@Composable
private fun DialogRow(label: String, isSelected: Boolean, onSelected: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier.clip(MaterialTheme.shapes.small)
                .selectable(selected = isSelected, onClick = { if (!isSelected) onSelected() })
                .fillMaxWidth()
                .minimumInteractiveComponentSize(),
    ) {
        ButtonMMD(
            onClick = { if (!isSelected) onSelected() },
            colors = if (isSelected) ButtonDefaultsMMD.buttonColors() else ButtonDefaultsMMD.outlinedButtonColors(),
        ) {
            TextMMD(
                text = label,
                style = MaterialTheme.typography.bodyLarge.merge(),
            )
        }
    }
}
