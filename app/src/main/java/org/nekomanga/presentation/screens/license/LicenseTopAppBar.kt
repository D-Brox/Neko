package org.nekomanga.presentation.screens.license

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.nekomanga.R
import org.nekomanga.presentation.components.bars.TitleTopAppBar
import org.nekomanga.presentation.functions.getTopAppBarColor

@Composable
fun LicenseTopAppBar(onNavigationClicked: () -> Unit) {

    val (color, onColor, _) = getTopAppBarColor(true, false)

    TitleTopAppBar(
        color = color,
        onColor = onColor,
        title = stringResource(R.string.open_source_licenses),
        navigationIconLabel = stringResource(R.string.back),
        navigationIcon = Icons.AutoMirrored.Default.ArrowBack,
        incognitoMode = false,
        onNavigationIconClicked = onNavigationClicked,
    )
}
