package org.nekomanga.presentation.components.dropdown

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import kotlinx.collections.immutable.PersistentList
import org.nekomanga.presentation.components.UiText
import org.nekomanga.presentation.components.theme.ThemeColorState

@Composable
fun SimpleDropdownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    dropDownItems: PersistentList<SimpleDropDownItem>,
    themeColorState: ThemeColorState,
) {
    NekoDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        themeColorState = themeColorState,
    ) {
        val enabledStyle =
            MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
        val disabledStyle = enabledStyle.copy(color = enabledStyle.color.copy(alpha = .38f))

        dropDownItems.forEach { item ->
            Row(
                item = item,
                enabledStyle = enabledStyle,
                disabledStyle = disabledStyle,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
private fun Row(
    item: SimpleDropDownItem,
    enabledStyle: TextStyle,
    disabledStyle: TextStyle,
    onDismiss: () -> Unit,
) {

    when (item) {
        is SimpleDropDownItem.Parent -> {
            // DropdownMenuMMD doesn't support cascading menus
            // Display parent as a regular item
            NekoDropdownMenuItem(
                text = item.text.asString(),
                onClick = {
                    onDismiss()
                    // Parent items typically don't have a direct action
                },
                enabled = item.enabled,
            )
        }
        is SimpleDropDownItem.Action -> {
            NekoDropdownMenuItem(
                text = item.text.asString(),
                onClick = {
                    onDismiss()
                    item.onClick()
                },
                enabled = item.enabled,
            )
        }
    }
}

@Immutable
sealed class SimpleDropDownItem {
    data class Action(val text: UiText, val enabled: Boolean = true, val onClick: () -> Unit) :
        SimpleDropDownItem()

    data class Parent(
        val text: UiText,
        val enabled: Boolean = true,
        val children: List<SimpleDropDownItem>,
    ) : SimpleDropDownItem()
}
