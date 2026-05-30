package io.github.rabehx.securify.ui.ext

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import io.github.rabehx.securify.datastore.model.ThemeMode
import io.github.rabehx.securify.datastore.model.UserPreferences

@Composable
fun UserPreferences.isDarkTheme(): Boolean {
    return when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
}
