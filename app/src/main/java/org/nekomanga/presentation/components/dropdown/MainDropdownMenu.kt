package org.nekomanga.presentation.components.dropdown

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import org.nekomanga.R
import org.nekomanga.presentation.components.Divider
import org.nekomanga.presentation.components.UiText
import org.nekomanga.presentation.components.icons.IncognitoIcon
import org.nekomanga.presentation.components.icons.IncognitoOffIcon
import org.nekomanga.presentation.components.theme.ThemeColorState
import org.nekomanga.presentation.components.theme.defaultThemeColorState

@Composable
fun MainDropdownMenu(
    themeColorState: ThemeColorState = defaultThemeColorState(),
    expanded: Boolean,
    incognitoModeEnabled: Boolean,
    incognitoModeClick: () -> Unit,
    settingsClick: () -> Unit,
    statsClick: () -> Unit,
    aboutClick: () -> Unit,
    onDismiss: () -> Unit,
) {

    val menuItems =
        remember(incognitoModeEnabled) {
            val (incognitoText, incognitoIcon) =
                if (incognitoModeEnabled) {
                    R.string.turn_off_incognito_mode to IncognitoOffIcon
                } else {
                    R.string.turn_on_incognito_mode to IncognitoIcon
                }

            listOf(
                DropdownMenuItemData(
                    title = UiText.StringResource(incognitoText),
                    subtitle = UiText.StringResource(R.string.pauses_reading_history),
                    icon = incognitoIcon,
                    onClick = incognitoModeClick,
                ),
                DropdownMenuItemData(
                    title = UiText.StringResource(R.string.settings),
                    icon = Icons.Outlined.Settings,
                    onClick = settingsClick,
                ),
                DropdownMenuItemData(
                    title = UiText.StringResource(R.string.stats),
                    icon = Icons.Outlined.QueryStats,
                    onClick = statsClick,
                ),
                DropdownMenuItemData(
                    title = UiText.StringResource(R.string.about),
                    icon = Icons.Outlined.Info,
                    onClick = aboutClick,
                ),
            )
        }

    NekoDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        themeColorState = themeColorState,
    ) {
        menuItems.forEachIndexed { index, item ->
            Row(
                title = item.title,
                subTitle = item.subtitle,
                icon = item.icon,
                onClick = {
                    onDismiss()
                    item.onClick()
                },
            )
            if (index == 0) {
                Divider()
            }
        }
    }
}

@Composable
private fun Row(title: UiText, subTitle: UiText? = null, icon: ImageVector, onClick: () -> Unit) {
    NekoDropdownMenuItem(text = title.asString(), onClick = onClick, leadingIcon = icon)
}

private data class DropdownMenuItemData(
    val title: UiText,
    val subtitle: UiText? = null,
    val icon: ImageVector,
    val onClick: () -> Unit,
)
