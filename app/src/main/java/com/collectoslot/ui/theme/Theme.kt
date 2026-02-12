package com.collectoslot.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CollectoSlotColorScheme = darkColorScheme(
    primary = Gold,
    secondary = CrimsonRed,
    tertiary = CasinoGreen,
    background = SlotBackground,
    surface = SlotDarkPurple,
    onPrimary = SlotBackground,
    onSecondary = Chrome,
    onBackground = Chrome,
    onSurface = Chrome,
)

@Composable
fun CollectoSlotTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CollectoSlotColorScheme,
        typography = CollectoSlotTypography,
        content = content
    )
}
