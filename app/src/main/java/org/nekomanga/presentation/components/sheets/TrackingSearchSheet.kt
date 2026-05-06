package org.nekomanga.presentation.components.sheets

import Header
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.mudita.mmd.components.lazy.LazyColumnMMD
import com.mudita.mmd.components.progress_indicator.CircularProgressIndicatorMMD
import com.mudita.mmd.components.text.TextMMD
import eu.kanade.tachiyomi.ui.manga.TrackingConstants.TrackSearchResult
import jp.wasabeef.gap.Gap
import org.nekomanga.R
import org.nekomanga.domain.track.TrackItem
import org.nekomanga.domain.track.TrackSearchItem
import org.nekomanga.domain.track.TrackServiceItem
import org.nekomanga.presentation.components.NekoColors
import org.nekomanga.presentation.components.SearchFooter
import org.nekomanga.presentation.components.dialog.TrackingSwitchDialog
import org.nekomanga.presentation.components.theme.ThemeColorState
import org.nekomanga.presentation.theme.Size

@Composable
fun TrackingSearchSheet(
    themeColorState: ThemeColorState,
    alreadySelectedTrack: TrackItem? = null,
    cancelClick: () -> Unit,
    title: String,
    trackSearchResult: TrackSearchResult,
    service: TrackServiceItem,
    trackingRemoved: (Boolean, TrackServiceItem) -> Unit,
    searchTracker: (String) -> Unit,
    openInBrowser: (String, String) -> Unit,
    trackSearchItemClick: (TrackSearchItem) -> Unit,
) {
    val maxLazyHeight = LocalConfiguration.current.screenHeightDp * .5

    var trackSearchItem by remember { mutableStateOf<TrackSearchItem?>(null) }

    Column(modifier = Modifier.fillMaxWidth().navigationBarsPadding().imePadding()) {
        Header(stringResource(id = R.string.select_an_entry), cancelClick)

        when (trackSearchResult) {
            is TrackSearchResult.Success -> {
                LazyColumnMMD(
                    modifier =
                        Modifier.fillMaxWidth().requiredHeightIn(Size.none, maxLazyHeight.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item { Gap(Size.tiny) }
                    if (
                        alreadySelectedTrack == null &&
                            trackSearchResult.hasMatchingId &&
                            trackSearchResult.trackSearchResult.size == 1
                    ) {
                        trackSearchItemClick(trackSearchResult.trackSearchResult.first())
                    }

                    items(trackSearchResult.trackSearchResult) { item: TrackSearchItem ->
                        TrackSearchItem(
                            themeColorState = themeColorState,
                            trackSearch = item,
                            alreadySelectedTrack = alreadySelectedTrack,
                            openInBrowser = openInBrowser,
                            trackSearchItemClick = {
                                if (alreadySelectedTrack == null) {
                                    trackSearchItemClick(it)
                                } else {
                                    trackSearchItem = item
                                }
                            },
                        )

                        if (trackSearchItem != null) {
                            TrackingSwitchDialog(
                                themeColorState = themeColorState,
                                name = stringResource(id = service.nameRes),
                                oldName = alreadySelectedTrack?.title ?: "",
                                newName = trackSearchItem!!.trackItem.title,
                                onConfirm = { alsoRemoveFromTracker, isReplacing ->
                                    trackingRemoved(alsoRemoveFromTracker, service)
                                    if (isReplacing) {
                                        trackSearchItemClick(trackSearchItem!!)
                                    }
                                    cancelClick()
                                },
                                onDismiss = { trackSearchItem = null },
                            )
                        }
                    }

                    item { Gap(Size.tiny) }
                }
            }
            else ->
                CenteredBox(
                    themeColorState = themeColorState,
                    trackSearchResult = trackSearchResult,
                )
        }
        var searchText by remember { mutableStateOf(title) }
        SearchFooter(
            themeColorState = themeColorState,
            labelText = stringResource(id = R.string.title),
            title = searchText,
            textChanged = { searchText = it },
            search = searchTracker,
        )
        Gap(Size.mediumLarge)
    }
}

@Composable
private fun CenteredBox(themeColorState: ThemeColorState, trackSearchResult: TrackSearchResult) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(Size.medium),
        contentAlignment = Alignment.Center,
    ) {
        when (trackSearchResult) {
            is TrackSearchResult.Loading ->
                CircularProgressIndicatorMMD(
                    color = themeColorState.primaryColor,
                    modifier = Modifier.size(32.dp),
                )
            is TrackSearchResult.NoResult ->
                TextMMD(
                    text = stringResource(id = R.string.no_results_found),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            is TrackSearchResult.Error -> TextMMD(text = trackSearchResult.errorMessage)
            else -> Unit
        }
    }
}

@Composable
private fun TrackSearchItem(
    themeColorState: ThemeColorState,
    trackSearch: TrackSearchItem,
    alreadySelectedTrack: TrackItem?,
    openInBrowser: (String, String) -> Unit,
    trackSearchItemClick: (TrackSearchItem) -> Unit,
) {
    val isSelected =
        alreadySelectedTrack != null &&
            alreadySelectedTrack.mediaId != 0L &&
            alreadySelectedTrack.mediaId == trackSearch.trackItem.mediaId

    val (backdropColor, outlineColor) =
        if (isSelected) {
            themeColorState.containerColor to themeColorState.primaryColor
        } else {
            MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.outline
        }

    OutlinedCard(
        modifier = Modifier.padding(horizontal = Size.small),
        border = CardDefaults.outlinedCardBorder(true).copy(brush = SolidColor(outlineColor)),
        onClick = { trackSearchItemClick(trackSearch) },
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            AsyncImage(
                model =
                    ImageRequest.Builder(LocalContext.current).data(trackSearch.coverUrl).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )

            Box(
                modifier =
                    Modifier.height(IntrinsicSize.Min)
                        .fillMaxWidth()
                        .background(
                            color = backdropColor.copy(alpha = NekoColors.highAlphaLowContrast)
                        )
            ) {
                IconButton(
                    onClick = {
                        openInBrowser(
                            trackSearch.trackItem.trackingUrl,
                            trackSearch.trackItem.title,
                        )
                    },
                    modifier = Modifier.padding(horizontal = Size.tiny).align(Alignment.TopEnd),
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInBrowser,
                        contentDescription = stringResource(id = R.string.open_in_browser),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Column(modifier = Modifier.padding(Size.small)) {
                    TextMMD(
                        text = trackSearch.trackItem.title,
                        style =
                            MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                        modifier = Modifier.fillMaxWidth(.9f),
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    if (trackSearch.publishingType.isNotEmpty()) {
                        Row {
                            TextMMD(
                                text = stringResource(id = R.string.type),
                                style =
                                    MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Gap(Size.tiny)
                            TextMMD(
                                text = trackSearch.publishingType,
                                color =
                                    MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = NekoColors.mediumAlphaHighContrast
                                    ),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }

                    if (trackSearch.startDate.isNotEmpty()) {
                        Row {
                            TextMMD(
                                text = stringResource(id = R.string.started),
                                style =
                                    MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                            )
                            Gap(Size.tiny)
                            TextMMD(
                                text = trackSearch.startDate,
                                color =
                                    MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = NekoColors.mediumAlphaHighContrast
                                    ),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }

                    val summary =
                        when (trackSearch.summary.isBlank()) {
                            true -> stringResource(id = R.string.no_description)
                            false -> trackSearch.summary
                        }

                    TextMMD(
                        text = summary,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        color =
                            MaterialTheme.colorScheme.onSurface.copy(
                                alpha = NekoColors.mediumAlphaHighContrast
                            ),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            if (isSelected) {
                Box(
                    modifier =
                        Modifier.align(Alignment.BottomEnd)
                            .padding(Size.small)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(color = outlineColor),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.selected),
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.surface,
                    )
                }
            }
        }
    }
}
