package org.nekomanga.presentation.components.dropdown

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import com.mudita.mmd.ThemeMMD
import com.mudita.mmd.components.menus.DropdownMenuItemMMD
import com.mudita.mmd.components.menus.DropdownMenuMMD
import org.nekomanga.presentation.components.theme.ThemeColorState
import org.nekomanga.presentation.theme.Size

@Composable
fun NekoDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    themeColorState: ThemeColorState,
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    val colors =
        MaterialTheme.colorScheme.copy(
            primary = themeColorState.primaryColor,
            surface = themeColorState.altContainerColor,
            surfaceContainer = themeColorState.altContainerColor,
            onSurface = themeColorState.onAltContainerColor,
            onSurfaceVariant = themeColorState.onAltContainerColor,
        )

    ThemeMMD() {
        DropdownMenuMMD(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            offset = DpOffset(Size.smedium, Size.none),
            modifier = modifier,
        ) {
            content()
        }
    }
}

@Composable
fun NekoDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    enabled: Boolean = true,
) {
    DropdownMenuItemMMD(
        text = { androidx.compose.material3.Text(text = text) },
        onClick = onClick,
        modifier = modifier,
        leadingIcon =
            if (leadingIcon != null) {
                {
                    androidx.compose.material3.Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                    )
                }
            } else {
                null
            },
        trailingIcon =
            if (trailingIcon != null) {
                {
                    androidx.compose.material3.Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                    )
                }
            } else {
                null
            },
        enabled = enabled,
    )
}
