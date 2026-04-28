package org.nekomanga.presentation.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true, apiLevel = 32)
@Composable
private fun DownloadUnreadBadgePreview() {
    Surface { DownloadUnreadBadge(true, 100, true, 100, 0.dp) }
}

@Preview(showBackground = true, apiLevel = 32)
@Composable
private fun DownloadUnreadBadgePreview10() {
    Surface { DownloadUnreadBadge(true, 10, true, 1, 0.dp) }
}

@Preview(showBackground = true, apiLevel = 32)
@Composable
private fun DownloadUnreadBadgePreviewSmall() {
    Surface { DownloadUnreadBadge(true, 1, true, 1, 0.dp) }
}

@Preview(showBackground = true, apiLevel = 32)
@Composable
private fun DownloadUnreadBadgePreview2() {
    Surface { DownloadUnreadBadge(true, 100, true, 100, 0.dp) }
}

@Preview(showBackground = true, apiLevel = 32)
@Composable
private fun DownloadUnreadBadgePreview3() {
    Surface { DownloadUnreadBadge(false, 100, true, 100, 0.dp) }
}

@Preview(showBackground = true, apiLevel = 32)
@Composable
private fun DownloadUnreadBadgePreview4() {
    Surface { DownloadUnreadBadge(true, 100, false, 100, 0.dp) }
}

@Preview(showBackground = true, apiLevel = 32)
@Composable
private fun DownloadUnreadBadgePreview5() {
    Surface { DownloadUnreadBadge(true, 1, false, 100, 0.dp) }
}
