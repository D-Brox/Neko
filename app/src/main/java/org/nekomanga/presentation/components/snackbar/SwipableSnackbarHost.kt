package org.nekomanga.presentation.components.snackbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.mudita.mmd.components.snackbar.SnackbarDataMMD
import com.mudita.mmd.components.snackbar.SnackbarHostMMD
import com.mudita.mmd.components.snackbar.SnackbarHostStateMMD
import com.mudita.mmd.components.snackbar.SnackbarMMD
import org.nekomanga.presentation.theme.Size

@Composable
fun SwipeableSnackbarHost(
    hostState: SnackbarHostStateMMD,
    modifier: Modifier = Modifier,
    snackbar: @Composable (SnackbarDataMMD) -> Unit = { SnackbarMMD(it) },
) {

    SnackbarHostMMD(
        hostState = hostState,
        modifier = modifier,
        snackbar = { snackbarData ->
            val dismissState = rememberSwipeToDismissBoxState()

            LaunchedEffect(dismissState.currentValue) {
                if (dismissState.currentValue != SwipeToDismissBoxValue.Settled) {
                    hostState.currentSnackbarData?.dismiss()
                }
            }

            // We must reset the dismiss state when a new snackbar appears
            LaunchedEffect(snackbarData) { dismissState.reset() }

            SwipeToDismissBox(
                state = dismissState,
                modifier = Modifier.padding(horizontal = Size.medium),
                backgroundContent = {},
                content = { snackbar(snackbarData) },
                enableDismissFromStartToEnd = true,
                enableDismissFromEndToStart = true,
            )
        },
    )
}
