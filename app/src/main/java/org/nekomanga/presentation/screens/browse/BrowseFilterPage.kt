package org.nekomanga.presentation.screens.browse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mudita.mmd.components.progress_indicator.LinearProgressIndicatorMMD
import eu.kanade.tachiyomi.ui.source.browse.DisplayMangaHolder
import org.nekomanga.R
import org.nekomanga.domain.manga.DisplayManga
import org.nekomanga.presentation.components.MangaGrid
import org.nekomanga.presentation.components.MangaList
import org.nekomanga.presentation.components.UiText
import org.nekomanga.presentation.functions.numberOfColumns
import org.nekomanga.presentation.screens.EmptyScreen

@Composable
fun BrowseFilterPage(
    displayMangaHolder: DisplayMangaHolder,
    isList: Boolean,
    isComfortableGrid: Boolean,
    dynamicCovers: Boolean,
    rawColumnCount: Float,
    pageLoading: Boolean,
    lastPage: Boolean,
    contentPadding: PaddingValues = PaddingValues(),
    onClick: (Long) -> Unit,
    onLongClick: (DisplayManga) -> Unit,
    loadNextPage: () -> Unit,
) {
    if (displayMangaHolder.allDisplayManga.isEmpty()) {
        EmptyScreen(
            message = UiText.StringResource(resourceId = R.string.no_results_found),
            contentPadding = contentPadding,
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isList) {
                MangaList(
                    mangaList = displayMangaHolder.filteredDisplayManga,
                    dynamicCover = dynamicCovers,
                    contentPadding = contentPadding,
                    onClick = onClick,
                    onLongClick = onLongClick,
                    lastPage = lastPage,
                    loadNextItems = loadNextPage,
                )
            } else {
                MangaGrid(
                    mangaList = displayMangaHolder.filteredDisplayManga,
                    dynamicCover = dynamicCovers,
                    contentPadding = contentPadding,
                    columns = numberOfColumns(rawValue = rawColumnCount),
                    isComfortable = isComfortableGrid,
                    onClick = onClick,
                    onLongClick = onLongClick,
                    lastPage = lastPage,
                    loadNextItems = loadNextPage,
                )
            }
            if (pageLoading) {
                LinearProgressIndicatorMMD(
                    progress = { 0.5F },
                    modifier =
                        Modifier.fillMaxWidth().align(Alignment.TopStart).statusBarsPadding(),
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}
