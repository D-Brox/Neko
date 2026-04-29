package org.nekomanga.presentation.screens.browse

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
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
        if (isList) {
            MangaList(
                mangaList = displayMangaHolder.filteredDisplayManga,
                dynamicCover = dynamicCovers,
                contentPadding = contentPadding,
                onClick = onClick,
                onLongClick = onLongClick,
                lastPage = lastPage,
                loadNextItems = loadNextPage,
                pageLoading = pageLoading,
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
                pageLoading = pageLoading,
            )
        }
    }
}
