package org.nekomanga.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mudita.mmd.components.text.TextMMD

@Preview
@Composable
private fun InfoScaffoldPreview() {
    InfoScreen(
        icon = Icons.Outlined.Newspaper,
        headingText = "Heading",
        subtitleText = "Subtitle",
        acceptText = "Accept",
        onAcceptClick = {},
        rejectText = "Reject",
        onRejectClick = {},
    ) {
        TextMMD("Hello world")
    }
}
