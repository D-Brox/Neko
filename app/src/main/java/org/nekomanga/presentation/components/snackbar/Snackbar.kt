package org.nekomanga.presentation.components.snackbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.mudita.mmd.components.snackbar.SnackbarHostStateMMD
import com.mudita.mmd.components.snackbar.SnackbarMMD
import com.mudita.mmd.components.text.TextMMD
import org.nekomanga.domain.snackbar.SnackbarColor
import org.nekomanga.presentation.theme.Size

@Composable
fun NekoSnackbarHost(
    snackbarHostState: SnackbarHostStateMMD,
    snackbarColor: SnackbarColor? = null,
) {
    SwipeableSnackbarHost(snackbarHostState) { data ->
        SnackbarMMD(
            dismissAction = {},
            action = {
                data.visuals.actionLabel?.let {
                    TextButton(onClick = { data.performAction() }) {
                        TextMMD(
                            text = data.visuals.actionLabel!!,
                            color =
                                snackbarColor?.actionColor ?: MaterialTheme.colorScheme.onSurface,
                            style =
                                MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                        )
                    }
                }
            },
            dismissActionContentColor = MaterialTheme.colorScheme.onSurface,
            containerColor =
                snackbarColor?.containerColor
                    ?: MaterialTheme.colorScheme.surfaceColorAtElevation(Size.small),
            contentColor = snackbarColor?.contentColor ?: MaterialTheme.colorScheme.onSurface,
        ) {
            TextMMD(text = data.visuals.message)
        }
    }
}
