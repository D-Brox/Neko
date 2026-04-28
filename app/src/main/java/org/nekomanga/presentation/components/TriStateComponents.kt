package org.nekomanga.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.mudita.mmd.components.checkbox.CheckboxDefaultsMMD
import com.mudita.mmd.components.checkbox.TriStateCheckboxMMD
import com.mudita.mmd.components.chips.FilterChipDefaultsMMD
import com.mudita.mmd.components.chips.FilterChipMMD
import com.mudita.mmd.components.text.TextMMD
import jp.wasabeef.gap.Gap
import org.nekomanga.presentation.components.theme.ThemeColorState
import org.nekomanga.presentation.components.theme.defaultThemeColorState
import org.nekomanga.presentation.extensions.surfaceColorAtElevationCustomColor
import org.nekomanga.presentation.theme.Size

@Composable
fun TriStateCheckboxRow(
    state: ToggleableState,
    toggleState: (ToggleableState) -> Unit,
    rowText: String,
    modifier: Modifier = Modifier,
    rowTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    disabled: Boolean = false,
    themeColorState: ThemeColorState = defaultThemeColorState(),
) {
    Row(
        modifier = modifier.clickable { toggleStateIfAble(disabled, state, toggleState) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TriStateCheckboxMMD(
            state = state,
            onClick = { toggleStateIfAble(disabled, state, toggleState) },
            enabled = !disabled,
            colors =
                CheckboxDefaultsMMD.colors(
                    checkedColor = themeColorState.primaryColor,
                    checkmarkColor = MaterialTheme.colorScheme.surface,
                ),
        )
        Gap(Size.tiny)
        TextMMD(
            text = rowText,
            color =
                if (!disabled) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurface.copy(NekoColors.disabledAlphaLowContrast),
            style = rowTextStyle,
        )
    }
}

@Composable
fun TriStateFilterChip(
    state: ToggleableState,
    toggleState: (ToggleableState) -> Unit,
    name: String,
    modifier: Modifier = Modifier,
    hideIcons: Boolean = false,
    labelTextStyle: TextStyle =
        MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
) {
    FilterChipMMD(
        modifier = modifier,
        selected = state == ToggleableState.On || state == ToggleableState.Indeterminate,
        onClick = { toggleStateIfAble(false, state, toggleState) },
        leadingIcon = {
            if (!hideIcons) {
                if (state == ToggleableState.On) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                } else if (state == ToggleableState.Indeterminate) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = null)
                }
            }
        },
        shape = RoundedCornerShape(100),
        label = { TextMMD(text = name, style = labelTextStyle) },
        colors =
            FilterChipDefaultsMMD.filterChipColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceColorAtElevationCustomColor(
                        MaterialTheme.colorScheme.primary,
                        Size.small,
                    ),
                labelColor = MaterialTheme.colorScheme.primary,
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
            ),
        border =
            FilterChipDefaultsMMD.filterChipBorder(
                enabled = true,
                selected = false,
                borderColor = MaterialTheme.colorScheme.primary.copy(NekoColors.veryLowContrast),
                selectedBorderColor = Color.Transparent,
            ),
    )
}

private fun toggleStateIfAble(
    disabled: Boolean,
    state: ToggleableState,
    toggleState: (ToggleableState) -> Unit,
) {
    if (!disabled) {
        val newState =
            when (state) {
                ToggleableState.On -> ToggleableState.Indeterminate
                ToggleableState.Indeterminate -> ToggleableState.Off
                ToggleableState.Off -> ToggleableState.On
            }
        toggleState(newState)
    }
}
