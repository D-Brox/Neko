package org.nekomanga.presentation.theme.colorschemes

import androidx.compose.material3.ColorScheme
import com.mudita.mmd.eInkColorScheme

object MMDColorScheme : BaseColorScheme() {
    override val darkScheme: ColorScheme = eInkColorScheme()
    override val lightScheme: ColorScheme = eInkColorScheme()
}
