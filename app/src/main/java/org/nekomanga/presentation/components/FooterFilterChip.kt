package org.nekomanga.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.mudita.mmd.components.chips.FilterChipDefaultsMMD
import com.mudita.mmd.components.chips.FilterChipMMD
import com.mudita.mmd.components.text.TextMMD
import org.nekomanga.presentation.extensions.surfaceColorAtElevationCustomColor
import org.nekomanga.presentation.theme.Size

@Composable
fun FooterFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    name: String,
    icon: ImageVector? = null,
) {
    FilterChipMMD(
        selected = selected,
        onClick = onClick,
        leadingIcon = {},
        shape = RoundedCornerShape(100),
        label = {
            when (icon != null) {
                true -> Icon(imageVector = icon, contentDescription = null)
                false -> {
                    TextMMD(
                        text = name,
                        style =
                            MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                    )
                }
            }
        },
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
