package org.nekomanga.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.mudita.mmd.components.text.TextMMD
import eu.kanade.tachiyomi.data.download.model.Download
import eu.kanade.tachiyomi.ui.manga.MangaConstants
import java.text.NumberFormat
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import org.nekomanga.R
import org.nekomanga.core.util.launchDelayed
import org.nekomanga.presentation.components.dropdown.SimpleDropDownItem
import org.nekomanga.presentation.components.dropdown.SimpleDropdownMenu
import org.nekomanga.presentation.components.theme.ThemeColorState
import org.nekomanga.presentation.components.theme.defaultThemeColorState
import org.nekomanga.presentation.theme.Size
import org.nekomanga.ui.theme.ThemeConfig
import org.nekomanga.ui.theme.ThemeConfigProvider
import org.nekomanga.ui.theme.ThemedPreviews

private const val iconSize = 20
private const val borderSize = 2.0

@Composable
fun DownloadButton(
    modifier: Modifier = Modifier,
    themeColorState: ThemeColorState = defaultThemeColorState(),
    downloadState: Download.State,
    downloadProgress: Int,
    defaultDisableColor: Boolean = false,
    onClick: (MangaConstants.DownloadAction) -> Unit,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        var showChapterDropdown by remember { mutableStateOf(false) }
        var isDraining by remember { mutableStateOf(false) }

        var downloadComplete by remember { mutableStateOf(false) }
        var previousDownloadState by remember { mutableStateOf(downloadState) }

        LaunchedEffect(downloadState) {
            if (
                previousDownloadState == Download.State.DOWNLOADING &&
                    downloadState == Download.State.DOWNLOADED
            ) {
                downloadComplete = true
            } else if (downloadState == Download.State.NOT_DOWNLOADED) {
                downloadComplete = false
            }
            previousDownloadState = downloadState
        }

        val downloadButtonLabel = stringResource(R.string.download)
        val optionsLabel = stringResource(R.string.options)
        val notDownloadedDescription = stringResource(R.string.not_downloaded)
        val queueDescription = stringResource(R.string.download_queue)
        val percentFormatter = remember { NumberFormat.getPercentInstance() }
        val downloadingDescription =
            stringResource(R.string.downloading_, percentFormatter.format(downloadProgress / 100f))
        val downloadedDescription = stringResource(R.string.downloaded)
        val errorDescription = stringResource(R.string.download_error)

        val downloadButtonModifier =
            Modifier.clickable(
                    onClick = {
                        when (downloadState) {
                            Download.State.NOT_DOWNLOADED ->
                                onClick(MangaConstants.DownloadAction.Download)
                            else -> showChapterDropdown = true
                        }
                    },
                    role = Role.Button,
                    onClickLabel =
                        when (downloadState) {
                            Download.State.NOT_DOWNLOADED -> downloadButtonLabel
                            else -> optionsLabel
                        },
                )
                .semantics {
                    contentDescription =
                        when (downloadState) {
                            Download.State.NOT_DOWNLOADED -> notDownloadedDescription
                            Download.State.QUEUE -> queueDescription
                            Download.State.DOWNLOADING -> downloadingDescription
                            Download.State.DOWNLOADED -> downloadedDescription
                            Download.State.ERROR -> errorDescription
                        }
                }

        when {
            isDraining -> {
                Draining(
                    buttonColor = themeColorState.primaryColor,
                    modifier = downloadButtonModifier,
                    onAnimationEnd = {
                        isDraining = false
                        onClick(MangaConstants.DownloadAction.Remove)
                    },
                )
            }
            downloadState == Download.State.ERROR ->
                NotDownloadedError(
                    buttonColor = MaterialTheme.colorScheme.error,
                    modifier = downloadButtonModifier,
                )
            downloadState == Download.State.NOT_DOWNLOADED ->
                NotDownloaded(
                    buttonColor = themeColorState.primaryColor,
                    modifier = downloadButtonModifier,
                    defaultDisableColor = defaultDisableColor,
                )
            downloadState == Download.State.QUEUE -> Queued(modifier = downloadButtonModifier)
            downloadState == Download.State.DOWNLOADED ->
                Downloaded(
                    buttonColor = themeColorState.primaryColor,
                    downloadComplete = downloadComplete,
                    modifier = downloadButtonModifier,
                    onAnimationComplete = { downloadComplete = false },
                )
            downloadState == Download.State.DOWNLOADING ->
                Downloading(
                    buttonColor = themeColorState.primaryColor,
                    modifier = downloadButtonModifier,
                    downloadProgress = downloadProgress,
                )
        }

        val scope = rememberCoroutineScope()
        val dropDownItems =
            when (downloadState) {
                Download.State.DOWNLOADED -> {
                    persistentListOf(
                        SimpleDropDownItem.Action(
                            text = UiText.StringResource(R.string.remove),
                            onClick = { isDraining = true },
                        )
                    )
                }
                Download.State.ERROR -> {
                    persistentListOf(
                        SimpleDropDownItem.Action(
                            text = UiText.StringResource(R.string.cancel),
                            onClick = {
                                scope.launchDelayed {
                                    onClick(MangaConstants.DownloadAction.Cancel)
                                }
                            },
                        )
                    )
                }
                Download.State.QUEUE,
                Download.State.DOWNLOADING -> {
                    persistentListOf(
                        SimpleDropDownItem.Action(
                            text = UiText.StringResource(R.string.start_downloading_now),
                            onClick = {
                                scope.launchDelayed {
                                    onClick(MangaConstants.DownloadAction.ImmediateDownload)
                                }
                            },
                        ),
                        SimpleDropDownItem.Action(
                            text = UiText.StringResource(R.string.cancel),
                            onClick = {
                                scope.launchDelayed {
                                    onClick(MangaConstants.DownloadAction.Cancel)
                                }
                            },
                        ),
                    )
                }
                Download.State.NOT_DOWNLOADED -> persistentListOf()
            }

        SimpleDropdownMenu(
            expanded = showChapterDropdown,
            themeColorState = themeColorState,
            onDismiss = { showChapterDropdown = false },
            dropDownItems = dropDownItems,
        )
    }
}

@Composable
private fun NotDownloaded(
    buttonColor: Color,
    modifier: Modifier,
    defaultDisableColor: Boolean = false,
) {

    val disabledColor =
        MaterialTheme.colorScheme.onSurface.copy(alpha = NekoColors.disabledAlphaLowContrast)
    val (color, alpha) =
        when (defaultDisableColor) {
            true -> disabledColor to NekoColors.disabledAlphaLowContrast
            false -> buttonColor to 1f
        }

    Background(
        color = Color.Transparent,
        borderStroke = BorderStroke(borderSize.dp, color),
        modifier = modifier,
    ) {
        DownloadIcon(
            color = color,
            alpha = alpha,
            icon = rememberVectorPainter(image = Icons.Filled.ArrowDownward),
        )
    }
}

@Composable
private fun NotDownloadedError(buttonColor: Color, modifier: Modifier) {

    Background(
        color = Color.Transparent,
        borderStroke = BorderStroke(borderSize.dp, buttonColor),
        shape = CircleShape,
        modifier = modifier,
    ) {
        TextMMD(text = "!", color = buttonColor, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun Downloaded(
    buttonColor: Color,
    downloadComplete: Boolean,
    modifier: Modifier,
    onAnimationComplete: () -> Unit,
) {
    if (downloadComplete) {
        DownloadToComplete(
            modifier = modifier,
            color = buttonColor,
            onAnimationComplete = onAnimationComplete,
        )
    } else {
        Background(color = buttonColor, modifier = modifier) {
            DownloadIcon(
                color = MaterialTheme.colorScheme.surface,
                icon = rememberVectorPainter(image = Icons.Filled.ArrowDownward),
            )
        }
    }
}

@Composable
private fun DownloadToComplete(modifier: Modifier, color: Color, onAnimationComplete: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(500)
        onAnimationComplete()
    }
    Background(color = color, modifier = modifier) {
        DownloadIcon(
            color = MaterialTheme.colorScheme.surface,
            icon = rememberVectorPainter(image = Icons.Filled.Check),
        )
    }
}

@Composable
private fun Draining(buttonColor: Color, modifier: Modifier, onAnimationEnd: () -> Unit) {
    var progress by remember { mutableStateOf(1f) }

    LaunchedEffect(key1 = Unit) {
        progress = 0f
        delay(2000) // Approximate time for "draining" effect
        onAnimationEnd()
    }

    Background(
        color = buttonColor.copy(alpha = .4f),
        borderStroke = BorderStroke(borderSize.dp, buttonColor),
        modifier = modifier,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val fillHeight = height * (1 - progress)

            val path = Path()
            path.moveTo(0f, fillHeight)

            val amplitude = height * 0.1f // Adjust for wave height
            val frequency = 2 * Math.PI / width // Adjust for wave frequency

            for (x in 0..width.toInt()) {
                val y = fillHeight + amplitude * kotlin.math.sin((x * frequency)).toFloat()
                path.lineTo(x.toFloat(), y)
            }

            path.lineTo(width, height)
            path.lineTo(0f, height)
            path.close()

            drawPath(path = path, color = buttonColor)
        }
    }
}

@Composable
private fun Queued(modifier: Modifier) {
    val disabledColor =
        MaterialTheme.colorScheme.onSurface.copy(alpha = NekoColors.disabledAlphaHighContrast)

    Background(
        color = Color.Transparent,
        borderStroke = BorderStroke(width = borderSize.dp, color = disabledColor),
        modifier = modifier,
    ) {}
}

@Composable
private fun Downloading(buttonColor: Color, modifier: Modifier, downloadProgress: Int) {
    val progress = downloadProgress / Download.MaxProgress.toFloat()
    val iconPainter = rememberVectorPainter(image = Icons.Filled.ArrowDownward)

    val iconColor =
        if (progress < 0.4f) {
            MaterialTheme.colorScheme.onSurface.copy(alpha = NekoColors.disabledAlphaLowContrast)
        } else {
            MaterialTheme.colorScheme.surface
        }

    Background(
        color = buttonColor.copy(alpha = .4f),
        borderStroke = BorderStroke(borderSize.dp, buttonColor),
        modifier = modifier,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val fillHeight = height * (1 - progress)

            val path = Path()
            path.moveTo(0f, fillHeight)

            val amplitude = height * 0.1f // Adjust for wave height
            val frequency = 2 * Math.PI / width // Adjust for wave frequency

            for (x in 0..width.toInt()) {
                val y = fillHeight + amplitude * kotlin.math.sin((x * frequency)).toFloat()
                path.lineTo(x.toFloat(), y)
            }

            path.lineTo(width, height)
            path.lineTo(0f, height)
            path.close()

            drawPath(path = path, color = buttonColor)
        }
        DownloadIcon(color = iconColor, icon = iconPainter, alpha = if (progress == 1f) 1f else 0f)
    }
}

@Composable
private fun DownloadIcon(
    modifier: Modifier = Modifier,
    color: Color,
    icon: Painter,
    alpha: Float = 1f,
) {
    Icon(
        painter = icon,
        contentDescription = null,
        modifier = Modifier.requiredSize(iconSize.dp).then(modifier),
        tint = color.copy(alpha = alpha),
    )
}

@Composable
private fun Background(
    color: Color,
    modifier: Modifier = Modifier,
    borderStroke: BorderStroke? = null,
    shape: Shape = CircleShape,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.clip(shape).size(Size.extraLarge + 4.dp).then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Preview
@Composable
private fun DownloadButtonPreview(
    @PreviewParameter(ThemeConfigProvider::class) themeConfig: ThemeConfig
) {
    ThemedPreviews(themeConfig) {
        Row(horizontalArrangement = Arrangement.spacedBy(Size.small)) {
            DownloadButton(
                downloadState = Download.State.NOT_DOWNLOADED,
                downloadProgress = 0,
                onClick = {},
            )
            DownloadButton(downloadState = Download.State.QUEUE, downloadProgress = 0, onClick = {})
            DownloadButton(
                downloadState = Download.State.DOWNLOADING,
                downloadProgress = 35,
                onClick = {},
            )
            DownloadButton(
                downloadState = Download.State.DOWNLOADED,
                downloadProgress = 100,
                onClick = {},
            )
            DownloadButton(downloadState = Download.State.ERROR, downloadProgress = 0, onClick = {})
        }
    }
}
