package org.nekomanga.presentation.screens.library

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mudita.mmd.components.text.TextMMD
import jp.wasabeef.gap.Gap
import org.nekomanga.R
import org.nekomanga.presentation.theme.Size

@Composable
fun RowScope.CategorySortButtons(
    enabled: Boolean = true,
    categorySortClick: () -> Unit,
    ascendingClick: () -> Unit,
    sortString: String,
    isAscending: Boolean,
    textColor: Color = MaterialTheme.colorScheme.primary,
    categoryIsRefreshing: Boolean,
    categoryRefreshClick: () -> Unit,
) {
    TextButton(enabled = enabled, onClick = categorySortClick) {
        TextMMD(
            text = sortString,
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
        )
    }
    Gap(4.dp)
    IconButton(
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(contentColor = textColor),
        onClick = ascendingClick,
    ) {
        Icon(
            imageVector =
                when {
                    isAscending -> Icons.Default.ArrowDownward
                    else -> Icons.Default.ArrowUpward
                },
            contentDescription = stringResource(id = R.string.sort),
            modifier = Modifier.size(Size.mediumLarge),
        )
    }
    Gap(4.dp)
    AnimatedContent(targetState = categoryIsRefreshing) { targetState ->
        when (targetState) {
            true -> {
                IconButton(
                    enabled = false,
                    colors = IconButtonDefaults.iconButtonColors(disabledContentColor = textColor),
                    onClick = {},
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(Size.mediumLarge),
                        strokeWidth = Size.extraTiny,
                    )
                }
            }
            false -> {
                IconButton(
                    enabled = enabled,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = textColor),
                    onClick = categoryRefreshClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(id = R.string.refresh),
                        modifier = Modifier.size(Size.mediumLarge),
                    )
                }
            }
        }
    }
}
