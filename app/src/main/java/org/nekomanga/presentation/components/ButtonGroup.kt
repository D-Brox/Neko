package org.nekomanga.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.mudita.mmd.components.buttons.ButtonMMD
import com.mudita.mmd.components.text.TextMMD
import org.nekomanga.ui.theme.ThemeConfig
import org.nekomanga.ui.theme.ThemeConfigProvider
import org.nekomanga.ui.theme.ThemedPreviews

@Composable
fun <T> ButtonGroup(
    items: List<T>,
    selectedItem: T,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.(T) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupSpacing),
    ) {
        items.forEach { item ->
            val isSelected = item == selectedItem
            ButtonMMD(enabled = !isSelected, onClick = { onItemClick(item) }) { content(item) }
        }
    }
}

@Preview
@Composable
private fun ButtonGroupPreview(
    @PreviewParameter(ThemeConfigProvider::class) themeConfig: ThemeConfig
) {
    ThemedPreviews(themeConfig) {
        // Define the items for the button group
        val timePeriods = listOf("Summary", "History", "Updates")

        // State to track the currently selected item
        var selectedPeriod by remember { mutableStateOf(timePeriods.first()) }

        ButtonGroup(
            items = timePeriods,
            selectedItem = selectedPeriod,
            onItemClick = { item -> selectedPeriod = item },
            modifier = Modifier.fillMaxWidth(0.8f),
        ) { item ->
            // The content for each button: a TextMMD composable
            TextMMD(text = item)
        }
    }
}

private val ButtonGroupSpacing = 4.dp
