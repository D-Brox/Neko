package org.nekomanga.presentation.screens.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.mudita.mmd.components.text.TextMMD
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import org.nekomanga.presentation.theme.Size

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun BasePreferenceWidget(
    modifier: Modifier = Modifier,
    title: String? = null,
    subcomponent: @Composable (ColumnScope.() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    widget: @Composable (() -> Unit)? = null,
) {
    val highlighted = LocalPreferenceHighlighted.current
    val minHeight = LocalPreferenceMinHeight.current
    Row(
        modifier =
            modifier
                .highlightBackground(highlighted)
                .sizeIn(minHeight = minHeight)
                .combinedClickable(
                    enabled = onClick != null || onLongClick != null,
                    onClick = { onClick?.invoke() },
                    onLongClick = { onLongClick?.invoke() },
                )
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier.padding(start = PrefsHorizontalPadding, end = Size.small),
                content = { icon() },
            )
        }
        Column(modifier = Modifier.weight(1f).padding(vertical = PrefsVerticalPadding)) {
            if (!title.isNullOrBlank()) {
                TextMMD(
                    modifier = Modifier.padding(horizontal = PrefsHorizontalPadding),
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style =
                        MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                )
            }
            subcomponent?.invoke(this)
        }
        if (widget != null) {
            Box(modifier = Modifier.padding(end = PrefsHorizontalPadding), content = { widget() })
        }
    }
}

internal fun Modifier.highlightBackground(highlighted: Boolean): Modifier = composed {
    LaunchedEffect(Unit) {
        if (highlighted) {
            delay(3.seconds)
        }
    }
    this
}

internal val TrailingWidgetBuffer = Size.medium
internal val PrefsHorizontalPadding = Size.medium
internal val PrefsVerticalPadding = Size.medium
