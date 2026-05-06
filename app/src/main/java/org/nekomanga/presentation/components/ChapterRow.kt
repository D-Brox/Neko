package org.nekomanga.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.mudita.mmd.components.text.TextMMD
import eu.kanade.tachiyomi.data.download.model.Download
import eu.kanade.tachiyomi.source.online.utils.MdLang
import eu.kanade.tachiyomi.ui.manga.MangaConstants
import eu.kanade.tachiyomi.util.chapter.ChapterUtil
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import jp.wasabeef.gap.Gap
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import org.nekomanga.R
import org.nekomanga.constants.Constants
import org.nekomanga.constants.MdConstants
import org.nekomanga.domain.chapter.ChapterItem
import org.nekomanga.presentation.components.dropdown.SimpleDropDownItem
import org.nekomanga.presentation.components.dropdown.SimpleDropdownMenu
import org.nekomanga.presentation.components.theme.ThemeColorState
import org.nekomanga.presentation.extensions.surfaceColorAtElevationCustomColor
import org.nekomanga.presentation.components.NekoColors
import org.nekomanga.presentation.theme.Size

@Composable
fun ChapterRow(
    themeColor: ThemeColorState,
    chapterItem: ChapterItem,
    shouldHideChapterTitles: Boolean = false,
    onClick: (ChapterItem) -> Unit,
    onBookmark: (ChapterItem) -> Unit,
    onRead: (ChapterItem) -> Unit,
    onWebView: (ChapterItem) -> Unit,
    onComment: (String) -> Unit,
    onDownload: (List<ChapterItem>, MangaConstants.DownloadAction) -> Unit,
    blockScanlator: (MangaConstants.BlockType, String) -> Unit,
    markPrevious: (ChapterItem, Boolean) -> Unit,
) {
    val (readIcon, readTextRes) =
        if (chapterItem.chapter.read) Icons.Filled.VisibilityOff to R.string.mark_as_unread
        else Icons.Filled.Visibility to R.string.mark_as_read
    val (bookmarkIcon, bookmarkTextRes) =
        if (chapterItem.chapter.bookmark) Icons.Filled.BookmarkRemove to R.string.remove_bookmark
        else Icons.Filled.BookmarkAdd to R.string.add_bookmark

    var isDropdownExpanded by remember { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    val dropdownItems =
        remember(
            chapterItem.chapter.isLocalSource(),
            chapterItem.chapter.isMergedChapter(),
            chapterItem.chapter.scanlator,
            chapterItem.chapter.uploader,
        ) {
            buildChapterDropdownItems(
                isLocal = chapterItem.chapter.isLocalSource(),
                isMerged = chapterItem.chapter.isMergedChapter(),
                scanlator = chapterItem.chapter.scanlator,
                uploader = chapterItem.chapter.uploader,
                onWebView = { onWebView(chapterItem) },
                onComment = { onComment(chapterItem.chapter.mangaDexChapterId) },
                markPrevious = { read -> markPrevious(chapterItem, read) },
                blockScanlator = blockScanlator,
            )
        }

    SimpleDropdownMenu(
        expanded = isDropdownExpanded,
        themeColorState = themeColor,
        onDismiss = { isDropdownExpanded = false },
        dropDownItems = dropdownItems,
    )

    Row(
        modifier =
            Modifier.fillMaxWidth()
                .combinedClickable(
                    onClick = { onClick(chapterItem) },
                    onLongClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        isDropdownExpanded = true
                    },
                    role = Role.Button,
                    onClickLabel = stringResource(id = R.string.start_reading),
                )
                .padding(start = Size.small, top = Size.small, bottom = Size.small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            ChapterTitle(
                shouldHideChapterTitles = shouldHideChapterTitles,
                title = chapterItem.chapter.name,
                chapterNumber = chapterItem.chapter.chapterNumber.toDouble(),
                isBookmarked = chapterItem.chapter.bookmark,
                isRead = chapterItem.chapter.read,
                textColor = MaterialTheme.colorScheme.onSurface,
                themeColor = themeColor.primaryColor,
            )

            val subtitleText =
                remember(
                    chapterItem.chapter.dateUpload,
                    chapterItem.chapter.read,
                    chapterItem.chapter.lastPageRead,
                    chapterItem.chapter.pagesLeft,
                    chapterItem.chapter.scanlator,
                    chapterItem.chapter.uploader,
                    chapterItem.chapter.isMergedChapter(),
                ) {
                    val statuses = mutableListOf<String>()
                    ChapterUtil.relativeDate(chapterItem.chapter.dateUpload)?.let {
                        statuses.add(it)
                    }
                    if (!chapterItem.chapter.read && chapterItem.chapter.lastPageRead > 0) {
                        if (chapterItem.chapter.pagesLeft > 0) {
                            statuses.add(
                                context.resources.getQuantityString(
                                    R.plurals.pages_left,
                                    chapterItem.chapter.pagesLeft,
                                    chapterItem.chapter.pagesLeft,
                                )
                            )
                        } else {
                            statuses.add(
                                context.getString(
                                    R.string.page_,
                                    chapterItem.chapter.lastPageRead + 1,
                                )
                            )
                        }
                    }
                    if (chapterItem.chapter.scanlator.isNotBlank()) {
                        if (chapterItem.chapter.scanlator == Constants.NO_GROUP)
                            statuses.add(chapterItem.chapter.uploader)
                        statuses.add(chapterItem.chapter.scanlator)
                        if (
                            chapterItem.chapter.isMergedChapter() &&
                                chapterItem.chapter.uploader.isNotBlank()
                        )
                            statuses.add(chapterItem.chapter.uploader)
                    }
                    statuses.joinToString(Constants.SEPARATOR)
                }

            ChapterSubtitle(
                subtitleText = subtitleText,
                language = chapterItem.chapter.language,
                textColor = MaterialTheme.colorScheme.onSurface.copy(alpha = NekoColors.mediumAlphaLowContrast),
            )
        }

        Row {
            // Read/Unread button
            Icon(
                imageVector = readIcon,
                contentDescription = stringResource(id = readTextRes),
                modifier = Modifier.size(Size.medium),
                tint = themeColor.primaryColor,
            )

            Gap(Size.small)

            // Bookmark button
            Icon(
                imageVector = bookmarkIcon,
                contentDescription = stringResource(id = bookmarkTextRes),
                modifier = Modifier.size(Size.medium),
                tint = themeColor.primaryColor,
            )

            Gap(Size.small)

            ChapterDownloadIndicator(
                isUnavailable = chapterItem.chapter.isUnavailable,
                scanlator = chapterItem.chapter.scanlator,
                downloadState = chapterItem.downloadState,
                downloadProgress = chapterItem.downloadProgress,
                onDownload = { action -> onDownload(listOf(chapterItem), action) },
                themeColorState = themeColor,
            )
        }
    }
}

@Composable
private fun ChapterTitle(
    shouldHideChapterTitles: Boolean,
    title: String,
    chapterNumber: Double,
    isBookmarked: Boolean,
    isRead: Boolean,
    textColor: Color,
    themeColor: Color,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isBookmarked) {
            Icon(
                imageVector = Icons.Filled.Bookmark,
                contentDescription = null,
                modifier = Modifier.size(Size.medium),
                tint = themeColor,
            )
            Gap(Size.tiny)
        }
        if (isRead) {
            Icon(
                imageVector = Icons.Filled.VisibilityOff,
                contentDescription = stringResource(id = R.string.mark_as_unread),
                modifier = Modifier.size(Size.medium),
                tint = textColor,
            )
            Gap(Size.tiny)
        }
        TextMMD(
            text =
                if (shouldHideChapterTitles)
                    stringResource(id = R.string.chapter_, decimalFormat.format(chapterNumber))
                else title,
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    color = textColor,
                    fontWeight = FontWeight.Medium,
                ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ChapterSubtitle(subtitleText: String, language: String, textColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (language.isNotBlank()) {
            LanguageIcon(language = language, textColor = textColor)
        }
        TextMMD(
            text = subtitleText,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    color = textColor,
                    fontWeight = FontWeight.Medium,
                ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun LanguageIcon(language: String, textColor: Color) {
    if (language.equals(MdLang.ENGLISH.lang, true)) return

    val mdLang = remember(language) { MdLang.fromIsoCode(language) }

    if (mdLang?.iconResId != null) {
        Image(
            painter = painterResource(id = mdLang.iconResId),
            modifier = Modifier.height(Size.medium).clip(RoundedCornerShape(Size.tiny)),
            contentDescription = mdLang.prettyPrint,
        )
        Spacer(modifier = Modifier.size(Size.tiny))
    } else {
        TextMMD(
            text = "$language?",
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    color = textColor,
                    fontWeight = FontWeight.Medium,
                ),
        )
        Spacer(modifier = Modifier.size(Size.tiny))
    }
}

@Composable
private fun ChapterDownloadIndicator(
    isUnavailable: Boolean,
    scanlator: String,
    downloadState: Download.State,
    downloadProgress: Int,
    onDownload: (MangaConstants.DownloadAction) -> Unit,
    themeColorState: ThemeColorState,
) {
    val isLocked = isUnavailable || MdConstants.UnsupportedOfficialGroupList.contains(scanlator)
    val isDownloaded = downloadState == Download.State.DOWNLOADED

    when {
        isLocked && !isDownloaded -> {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = stringResource(id = R.string.unavailable),
                modifier = Modifier.padding(Size.extraTiny).size(Size.extraLarge),
                tint = themeColorState.primaryColor,
            )
        }
        else -> {
            DownloadButton(
                themeColorState = themeColorState,
                modifier = Modifier,
                downloadState = downloadState,
                downloadProgress = downloadProgress,
                onClick = onDownload,
            )
        }
    }
}

private fun buildChapterDropdownItems(
    isLocal: Boolean,
    isMerged: Boolean,
    scanlator: String,
    uploader: String,
    onWebView: () -> Unit,
    onComment: () -> Unit,
    markPrevious: (Boolean) -> Unit,
    blockScanlator: (MangaConstants.BlockType, String) -> Unit,
): PersistentList<SimpleDropDownItem> {
    return buildList {
        if (!isLocal) {
            add(
                SimpleDropDownItem.Action(
                    text = UiText.StringResource(R.string.open_in_webview),
                    onClick = onWebView,
                )
            )
        }

        add(
            SimpleDropDownItem.Parent(
                text = UiText.StringResource(R.string.mark_previous_as),
                children =
                    listOf(
                        SimpleDropDownItem.Action(
                            text = UiText.StringResource(R.string.read),
                            onClick = { markPrevious(true) },
                        ),
                        SimpleDropDownItem.Action(
                            text = UiText.StringResource(R.string.unread),
                            onClick = { markPrevious(false) },
                        ),
                    ),
            )
        )

        val scanlatorItems =
            ChapterUtil.getScanlators(scanlator)
                .mapNotNull { name ->
                    if (name == Constants.NO_GROUP) uploader.takeIf { it.isNotBlank() }
                    else name
                }
                .map { name ->
                    SimpleDropDownItem.Action(
                        text = UiText.String(name),
                        onClick = {
                            val type =
                                if (name == uploader) MangaConstants.BlockType.Uploader
                                else MangaConstants.BlockType.Group
                            blockScanlator(type, name)
                        },
                    )
                }

        if (scanlatorItems.isNotEmpty() && !isLocal) {
            add(
                SimpleDropDownItem.Parent(
                    text = UiText.StringResource(R.string.block_scanlator),
                    children = scanlatorItems,
                )
            )
        }

        if (!isMerged && !isLocal) {
            add(
                SimpleDropDownItem.Action(
                    text = UiText.StringResource(R.string.comments),
                    onClick = onComment,
                )
            )
        }
    }
        .toPersistentList()
}

private val decimalFormat =
    DecimalFormat("#.###", DecimalFormatSymbols().apply { decimalSeparator = '.' })
