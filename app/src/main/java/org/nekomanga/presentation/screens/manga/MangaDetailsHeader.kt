package org.nekomanga.presentation.screens.manga

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import com.mudita.mmd.components.lazy.LazyColumnMMD
import com.mudita.mmd.components.text.TextMMD
import eu.kanade.tachiyomi.ui.manga.MangaConstants
import eu.kanade.tachiyomi.ui.manga.MangaConstants.DescriptionActions
import eu.kanade.tachiyomi.ui.manga.MangaConstants.InformationActions
import eu.kanade.tachiyomi.ui.manga.MergeConstants
import jp.wasabeef.gap.Gap
import org.nekomanga.presentation.components.MangaCover
import org.nekomanga.presentation.components.theme.ThemeColorState
import org.nekomanga.presentation.theme.Size

@Composable
fun MangaDetailsHeader(
    mangaDetailScreenState: MangaConstants.MangaDetailScreenState,
    isInitialized: Boolean,
    windowSizeClass: WindowSizeClass,
    isLoggedIntoTrackers: Boolean,
    themeColorState: ThemeColorState,
    generatePalette: (android.graphics.drawable.Drawable) -> Unit,
    toggleFavorite: () -> Unit,
    onCategoriesClick: () -> Unit,
    onTrackingClick: () -> Unit,
    onArtworkClick: () -> Unit,
    onSimilarClick: () -> Unit,
    onMergeClick: () -> Unit,
    onLinksClick: () -> Unit,
    onShareClick: () -> Unit,
    descriptionActions: DescriptionActions,
    informationActions: InformationActions,
    onQuickReadClick: () -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumnMMD(state = listState, modifier = Modifier.fillMaxHeight().clipToBounds()) {
        // Top section: Cover on left, InfoBlock on right
        item {
            Row(
                modifier =
                    Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = Size.small)
            ) {
                // Left half: Cover
                Box(
                    modifier = Modifier.weight(1f).padding(end = Size.small),
                    contentAlignment = Alignment.TopStart,
                ) {
                    MangaCover.Book(
                        artwork = mangaDetailScreenState.manga.currentArtwork,
                        dynamicCover = mangaDetailScreenState.manga.dynamicCovers,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                // Right half: InformationBlock + ButtonBlock
                Column(modifier = Modifier.weight(2f).padding(start = Size.small)) {
                    InformationBlock(
                        themeColorState = themeColorState,
                        title = mangaDetailScreenState.manga.currentTitle,
                        author = mangaDetailScreenState.manga.author,
                        artist = mangaDetailScreenState.manga.artist,
                        stats = mangaDetailScreenState.manga.stats,
                        langFlag = mangaDetailScreenState.manga.langFlag,
                        status = mangaDetailScreenState.manga.status,
                        lastChapter =
                            mangaDetailScreenState.manga.lastVolume to
                                mangaDetailScreenState.manga.lastChapter,
                        isPornographic = mangaDetailScreenState.manga.isPornographic,
                        missingChapters = mangaDetailScreenState.manga.missingChapters,
                        estimatedMissingChapters =
                            mangaDetailScreenState.manga.estimatedMissingChapters,
                        showMergedIcon =
                            mangaDetailScreenState.manga.isMerged is
                                MergeConstants.IsMergedManga.Yes &&
                                !mangaDetailScreenState.general.hideButtonText,
                        titleLongClick = informationActions.titleLongClick,
                        creatorCopyClick = informationActions.creatorCopy,
                        creatorSearchClick = informationActions.creatorSearch,
                    )
                    if (!mangaDetailScreenState.general.isSearching) {
                        Gap(Size.medium)

                        ButtonBlock(
                            hideButtonText = mangaDetailScreenState.general.hideButtonText,
                            isMerged =
                                mangaDetailScreenState.manga.isMerged
                                    is MergeConstants.IsMergedManga.Yes,
                            inLibrary = mangaDetailScreenState.manga.inLibrary,
                            loggedIntoTrackers = isLoggedIntoTrackers,
                            trackServiceCount = mangaDetailScreenState.track.trackServiceCount,
                            themeColorState = themeColorState,
                            toggleFavorite = toggleFavorite,
                            trackingClick = onTrackingClick,
                            artworkClick = onArtworkClick,
                            similarClick = onSimilarClick,
                            mergeClick = onMergeClick,
                            linksClick = onLinksClick,
                            shareClick = onShareClick,
                            moveCategories = onCategoriesClick,
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.size(Size.medium)) }

        // DescriptionBlock below
        if (
            mangaDetailScreenState.manga.initialized && !mangaDetailScreenState.general.isSearching
        ) {
            item {
                DescriptionBlock(
                    windowSizeClass = windowSizeClass,
                    title = mangaDetailScreenState.manga.currentTitle,
                    description = mangaDetailScreenState.manga.currentDescription,
                    altTitles = mangaDetailScreenState.manga.alternativeTitles,
                    genres = mangaDetailScreenState.manga.genres,
                    themeColorState = themeColorState,
                    wrapAltTitles = mangaDetailScreenState.general.wrapAltTitles,
                    expandCollapseClick = {},
                    descriptionActions = descriptionActions,
                )
            }

            item { Gap(Size.tiny) }
        }
    }
}
