package org.nekomanga.presentation.theme

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import com.mudita.mmd.ThemeMMD
import com.mudita.mmd.eInkColorScheme
import com.mudita.mmd.eInkTypography
import eu.kanade.tachiyomi.data.preference.PreferencesHelper
import org.nekomanga.presentation.theme.colorschemes.BlueColorScheme
import org.nekomanga.presentation.theme.colorschemes.BrownColorScheme
import org.nekomanga.presentation.theme.colorschemes.GreenColorScheme
import org.nekomanga.presentation.theme.colorschemes.MMDColorScheme
import org.nekomanga.presentation.theme.colorschemes.MonetColorScheme
import org.nekomanga.presentation.theme.colorschemes.MonochromeColorScheme
import org.nekomanga.presentation.theme.colorschemes.NekoColorScheme
import org.nekomanga.presentation.theme.colorschemes.NeonColorScheme
import org.nekomanga.presentation.theme.colorschemes.NordColorScheme
import org.nekomanga.presentation.theme.colorschemes.OrangeColorScheme
import org.nekomanga.presentation.theme.colorschemes.PinkColorScheme
import org.nekomanga.presentation.theme.colorschemes.PurpleColorScheme
import org.nekomanga.presentation.theme.colorschemes.RetroColorScheme
import org.nekomanga.presentation.theme.colorschemes.TakoColorScheme
import org.nekomanga.presentation.theme.colorschemes.TealColorScheme
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

@Composable
fun NekoTheme(colorScheme: ColorScheme? = null, content: @Composable () -> Unit) {
    ThemeMMD {
        // ThemeMMD wraps MaterialTheme with MMD optimizations (no ripples, monochrome, etc.)
        // Use standard Material3 components - they'll inherit MMD theme
        MaterialTheme(colorScheme = eInkColorScheme, typography = eInkTypography, content = content)
    }
}

@Composable
@ReadOnlyComposable
fun nekoThemeColorScheme(): ColorScheme {
    val preferences = Injekt.get<PreferencesHelper>()

    val isDarkMode =
        (isSystemInDarkTheme() ||
            preferences.nightMode().get() == AppCompatDelegate.MODE_NIGHT_YES) &&
            preferences.nightMode().get() != AppCompatDelegate.MODE_NIGHT_NO

    val theme =
        if (isDarkMode) {
                preferences.darkTheme()
            } else {
                preferences.lightTheme()
            }
            .get()

    return colorSchemeFromTheme(LocalContext.current, theme, isDarkMode)
}

fun colorSchemeFromTheme(
    context: Context,
    theme: Themes,
    isSystemInDarkTheme: Boolean,
): ColorScheme {
    return when (theme) {
        Themes.Blue -> BlueColorScheme.getColorScheme(isSystemInDarkTheme)
        Themes.Teal -> TealColorScheme.getColorScheme(isSystemInDarkTheme)
        Themes.Neon -> NeonColorScheme.getColorScheme(isSystemInDarkTheme)
        Themes.Green -> GreenColorScheme.getColorScheme(isSystemInDarkTheme)
        Themes.Monet -> MonetColorScheme(context).getColorScheme(isSystemInDarkTheme)
        Themes.Monochrome -> MonochromeColorScheme.getColorScheme(isSystemInDarkTheme)
        Themes.Nord -> NordColorScheme.getColorScheme(isSystemInDarkTheme)
        Themes.Orange -> OrangeColorScheme.getColorScheme(isSystemInDarkTheme)
        Themes.Pink -> PinkColorScheme.getColorScheme(isSystemInDarkTheme)
        Themes.Purple -> PurpleColorScheme.getColorScheme(isSystemInDarkTheme)
        Themes.Retro -> RetroColorScheme.getColorScheme(isSystemInDarkTheme)
        Themes.Brown -> BrownColorScheme.getColorScheme(isSystemInDarkTheme)
        Themes.Tako -> TakoColorScheme.getColorScheme(isSystemInDarkTheme)
        Themes.MMD -> MMDColorScheme.getColorScheme(isSystemInDarkTheme)
        else -> NekoColorScheme.getColorScheme(isSystemInDarkTheme)
    }
}
