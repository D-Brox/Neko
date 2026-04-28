package org.nekomanga.presentation.components.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import com.mudita.mmd.components.buttons.ButtonDefaultsMMD
import com.mudita.mmd.components.divider.HorizontalDividerMMD
import com.mudita.mmd.components.radio_button.RadioButtonMMD
import com.mudita.mmd.components.slider.SliderMMD
import com.mudita.mmd.components.switcher.SwitchMMD
import com.mudita.mmd.components.text.TextMMD
import eu.kanade.tachiyomi.ui.library.LibraryDisplayMode
import eu.kanade.tachiyomi.util.system.isLandscape
import jp.wasabeef.gap.Gap
import kotlin.math.roundToInt
import org.nekomanga.R
import org.nekomanga.constants.Constants
import org.nekomanga.presentation.components.theme.ThemeColorState
import org.nekomanga.presentation.components.theme.defaultThemeColorState
import org.nekomanga.presentation.functions.numberOfColumns
import org.nekomanga.presentation.theme.Size

@Composable
fun DisplayOptionsSheet(
    currentLibraryDisplayMode: LibraryDisplayMode,
    libraryDisplayModeClick: (LibraryDisplayMode) -> Unit,
    rawColumnCount: Float,
    rawColumnCountChanged: (Float) -> Unit,
    unreadBadgesEnabled: Boolean,
    unreadBadgesToggled: () -> Unit,
    downloadBadgesEnabled: Boolean,
    downloadBadgesToggled: () -> Unit,
    showStartReadingButtonEnabled: Boolean,
    startReadingButtonToggled: () -> Unit,
    horizontalCategoriesEnabled: Boolean,
    horizontalCategoriesToggled: () -> Unit,
    showLibraryButtonBarEnabled: Boolean,
    showLibraryButtonBarToggled: () -> Unit,
    themeColorState: ThemeColorState = defaultThemeColorState(),
) {
    CompositionLocalProvider(
        LocalRippleConfiguration provides themeColorState.rippleConfiguration
    ) {
        BaseSheet(themeColor = themeColorState) {
            val paddingModifier = Modifier.padding(horizontal = Size.small)

            Gap(Size.small)
            TextMMD(
                modifier = paddingModifier.fillMaxWidth(),
                text = stringResource(R.string.display_options),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            Gap(Size.large)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Size.medium),
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = Size.medium).selectableGroup(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    LibraryDisplayMode.entries().forEach { libraryDisplayMode ->
                        Row(
                            Modifier.weight(
                                if (libraryDisplayMode == LibraryDisplayMode.ComfortableGrid) 1.25f
                                else 1f
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            RadioButtonMMD(
                                selected = libraryDisplayMode == currentLibraryDisplayMode,
                                onClick = { libraryDisplayModeClick(libraryDisplayMode) },
                            )
                            Gap(Size.tiny)
                            TextMMD(
                                text = libraryDisplayMode.toUiText().asString(),
                                modifier =
                                    Modifier.clickable(
                                        role = Role.RadioButton,
                                        onClick = { libraryDisplayModeClick(libraryDisplayMode) },
                                    ),
                            )
                        }
                    }
                }

                if (currentLibraryDisplayMode != LibraryDisplayMode.List) {
                    var sliderPosition by rememberSaveable {
                        mutableFloatStateOf(((rawColumnCount + .5f) * 2f).roundToInt().toFloat())
                    }

                    Column(modifier = Modifier.padding(horizontal = Size.medium)) {
                        val context = LocalContext.current
                        val isPortrait = !context.isLandscape()
                        val numberOfColumns =
                            numberOfColumns(rawValue = sliderPosition, forText = true)
                        val numberOfColumnsAlt =
                            numberOfColumns(
                                rawValue = sliderPosition,
                                forText = true,
                                useHeight = true,
                            )
                        TextMMD(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text =
                                "Grid size: Portrait: ${if (isPortrait) numberOfColumns else numberOfColumnsAlt} ${Constants.SEPARATOR} Landscape: ${if (isPortrait) numberOfColumnsAlt else numberOfColumns}",
                        )
                        Gap(Size.tiny)
                        Row(modifier = Modifier.fillMaxWidth()) {
                            SliderMMD(
                                modifier = Modifier.weight(1f),
                                value = sliderPosition,
                                onValueChange = { sliderPosition = it },
                                onValueChangeFinished = {
                                    rawColumnCountChanged((sliderPosition / 2f) - .5f)
                                },
                                steps = 8,
                                valueRange = 0f..7f,
                            )
                            Gap(Size.tiny)
                            TextButton(
                                onClick = {
                                    sliderPosition = 3f
                                    rawColumnCountChanged((sliderPosition / 2f) - .5f)
                                },
                                shape = ButtonDefaultsMMD.shape,
                            ) {
                                TextMMD(stringResource(R.string.reset))
                            }
                        }
                    }
                }

                HorizontalDividerMMD()

                ToggleRow(
                    enabled = unreadBadgesEnabled,
                    onClick = unreadBadgesToggled,
                    text = stringResource(id = R.string.unread_badge),
                )

                ToggleRow(
                    enabled = downloadBadgesEnabled,
                    onClick = downloadBadgesToggled,
                    text = stringResource(R.string.download_badge),
                )

                ToggleRow(
                    enabled = showStartReadingButtonEnabled,
                    onClick = startReadingButtonToggled,
                    text = stringResource(R.string.show_start_reading_button),
                )
                HorizontalDividerMMD()
                ToggleRow(
                    enabled = showLibraryButtonBarEnabled,
                    onClick = showLibraryButtonBarToggled,
                    text = stringResource(R.string.show_library_action_bar),
                )

                ToggleRow(
                    enabled = horizontalCategoriesEnabled,
                    onClick = horizontalCategoriesToggled,
                    text = stringResource(R.string.horizontal_categories),
                )
            }
        }
    }
}

@Composable
private fun ToggleRow(enabled: Boolean, onClick: () -> Unit, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Gap(Size.small)
        TextMMD(modifier = Modifier.weight(1f), text = text)
        SwitchMMD(checked = enabled, onCheckedChange = { onClick() })
        Gap(Size.small)
    }
}
