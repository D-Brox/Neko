package org.nekomanga.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mudita.mmd.components.progress_indicator.LinearProgressIndicatorMMD

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullRefresh(
    enabled: Boolean = true,
    isRefreshing: Boolean,
    onRefresh: (() -> Unit)?,
    trackColor: Color = MaterialTheme.colorScheme.secondary,
    content: @Composable (Modifier) -> Unit,
) {
    if (enabled && onRefresh != null) {
        val state = rememberPullToRefreshState()

        Box(modifier = Modifier.fillMaxSize()) {
            content(
                Modifier.pullToRefresh(
                    state = state,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                )
            )

            WavyLinearIndicator(
                state = state,
                isRefreshing = isRefreshing,
                color = trackColor,
                modifier = Modifier.align(Alignment.TopCenter).statusBarsPadding(),
            )
        }
    } else {
        content(Modifier)
    }
}

@Composable
private fun WavyLinearIndicator(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val targetProgress = state.distanceFraction.coerceIn(0f, 1f)

    PullToRefreshDefaults.IndicatorBox(
        state = state,
        isRefreshing = isRefreshing,
        modifier = modifier,
    ) {
        LinearProgressIndicatorMMD(
            modifier = Modifier.fillMaxWidth(),
            color = color,
            progress = { if (isRefreshing) 0f else targetProgress },
        )
    }
}
