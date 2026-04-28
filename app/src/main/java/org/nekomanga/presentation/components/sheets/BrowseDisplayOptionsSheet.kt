package org.nekomanga.presentation.components.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mudita.mmd.components.lazy.LazyColumnMMD
import com.mudita.mmd.components.radio_button.RadioButtonMMD
import com.mudita.mmd.components.text.TextMMD
import eu.kanade.tachiyomi.ui.source.browse.LibraryEntryVisibility
import jp.wasabeef.gap.Gap
import org.nekomanga.R
import org.nekomanga.presentation.components.theme.ThemeColorState
import org.nekomanga.presentation.components.theme.defaultThemeColorState
import org.nekomanga.presentation.theme.Size

@Composable
fun BrowseDisplayOptionsSheet(
    modifier: Modifier = Modifier,
    showIsList: Boolean = false,
    isList: Boolean,
    switchDisplayClick: () -> Unit,
    currentLibraryEntryVisibility: Int,
    libraryEntryVisibilityClick: (Int) -> Unit,
    themeColorState: ThemeColorState = defaultThemeColorState(),
    bottomContentPadding: Dp = Size.medium,
) {
    CompositionLocalProvider(
        LocalRippleConfiguration provides themeColorState.rippleConfiguration
    ) {
        val maxLazyHeight = LocalConfiguration.current.screenHeightDp * .9

        BaseSheet(themeColor = themeColorState, bottomPaddingAroundContent = bottomContentPadding) {
            val paddingModifier = Modifier.padding(horizontal = Size.small)

            Gap(Size.small)
            TextMMD(
                modifier = paddingModifier.fillMaxWidth(),
                text = stringResource(R.string.display_options),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            Gap(Size.large)
            LazyColumnMMD(
                modifier = Modifier.fillMaxWidth().requiredHeightIn(Size.none, maxLazyHeight.dp),
                verticalArrangement = Arrangement.spacedBy(Size.medium),
            ) {
                if (showIsList) {
                    item {
                        TextMMD(
                            text = stringResource(R.string.display_as),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = Size.medium),
                        )
                    }
                    item {
                        Row(
                            Modifier.fillMaxWidth()
                                .padding(horizontal = Size.medium)
                                .selectableGroup(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Row(
                                Modifier.weight(1f),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                RadioButtonMMD(selected = isList, onClick = switchDisplayClick)
                                Gap(Size.tiny)
                                TextMMD(
                                    text = stringResource(R.string.list),
                                    modifier =
                                        Modifier.padding(horizontal = Size.medium).clickable(
                                            role = androidx.compose.ui.semantics.Role.RadioButton
                                        ) {
                                            switchDisplayClick()
                                        },
                                )
                            }
                            Row(
                                Modifier.weight(1f),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                RadioButtonMMD(selected = !isList, onClick = switchDisplayClick)
                                Gap(Size.tiny)
                                TextMMD(
                                    text = stringResource(R.string.grid),
                                    modifier =
                                        Modifier.padding(horizontal = Size.medium).clickable(
                                            role = androidx.compose.ui.semantics.Role.RadioButton
                                        ) {
                                            switchDisplayClick()
                                        },
                                )
                            }
                        }
                    }
                }
                item {
                    TextMMD(
                        text = stringResource(R.string.filter_results),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = Size.medium),
                    )
                }

                item {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = Size.medium).selectableGroup(),
                        horizontalArrangement = Arrangement.spacedBy(Size.small),
                    ) {
                        Row(
                            Modifier.weight(1f),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        ) {
                            RadioButtonMMD(
                                selected =
                                    currentLibraryEntryVisibility ==
                                        LibraryEntryVisibility.SHOW_NOT_IN_LIBRARY,
                                onClick = {
                                    libraryEntryVisibilityClick(
                                        LibraryEntryVisibility.SHOW_NOT_IN_LIBRARY
                                    )
                                },
                            )
                            Gap(Size.tiny)
                            TextMMD(
                                text = stringResource(R.string.hide_library_manga),
                                modifier =
                                    Modifier.clickable(
                                        role = androidx.compose.ui.semantics.Role.RadioButton
                                    ) {
                                        libraryEntryVisibilityClick(
                                            LibraryEntryVisibility.SHOW_NOT_IN_LIBRARY
                                        )
                                    },
                            )
                        }
                        Row(
                            Modifier.weight(1f),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        ) {
                            RadioButtonMMD(
                                selected =
                                    currentLibraryEntryVisibility ==
                                        LibraryEntryVisibility.SHOW_ALL,
                                onClick = {
                                    libraryEntryVisibilityClick(LibraryEntryVisibility.SHOW_ALL)
                                },
                            )
                            Gap(Size.tiny)
                            TextMMD(
                                text = stringResource(R.string.show_all_manga),
                                modifier =
                                    Modifier.clickable(
                                        role = androidx.compose.ui.semantics.Role.RadioButton
                                    ) {
                                        libraryEntryVisibilityClick(LibraryEntryVisibility.SHOW_ALL)
                                    },
                            )
                        }
                        Row(
                            Modifier.weight(1f),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        ) {
                            RadioButtonMMD(
                                selected =
                                    currentLibraryEntryVisibility ==
                                        LibraryEntryVisibility.SHOW_IN_LIBRARY,
                                onClick = {
                                    libraryEntryVisibilityClick(
                                        LibraryEntryVisibility.SHOW_IN_LIBRARY
                                    )
                                },
                            )
                            Gap(Size.tiny)
                            TextMMD(
                                text = stringResource(R.string.show_library_manga),
                                modifier =
                                    Modifier.clickable(
                                        role = androidx.compose.ui.semantics.Role.RadioButton
                                    ) {
                                        libraryEntryVisibilityClick(
                                            LibraryEntryVisibility.SHOW_IN_LIBRARY
                                        )
                                    },
                            )
                        }
                    }
                }
            }
        }
    }
}
