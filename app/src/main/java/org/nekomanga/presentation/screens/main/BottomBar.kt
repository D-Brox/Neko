package org.nekomanga.presentation.screens.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.mudita.mmd.components.nav_bar.NavigationBarItemMMD
import com.mudita.mmd.components.nav_bar.NavigationBarMMD
import com.mudita.mmd.components.text.TextMMD
import eu.kanade.tachiyomi.ui.main.NavigationItem

@Composable
fun BottomBar(
    items: List<NavigationItem>,
    libraryUpdating: Boolean,
    downloaderRunning: Boolean,
    selectedItemIndex: Int,
    onNavigate: (NavKey) -> Unit,
) {

    NavigationBarMMD(
        modifier = Modifier.fillMaxWidth(),
        content = {
            items.forEachIndexed { index, item ->
                NavigationBarItemMMD(
                    selected = selectedItemIndex == index,
                    onClick = { onNavigate(item.screen) },
                    icon = {
                        PulsingIcon(
                            isPulsing =
                                ((index == 0 && libraryUpdating) ||
                                    (index == 1 && downloaderRunning)),
                            imageVector =
                                if (selectedItemIndex == index) item.selectedIcon
                                else item.unselectedIcon,
                            contentDescription = null,
                        )
                    },
                    label = { TextMMD(text = item.title) },
                )
            }
        },
    )
}
