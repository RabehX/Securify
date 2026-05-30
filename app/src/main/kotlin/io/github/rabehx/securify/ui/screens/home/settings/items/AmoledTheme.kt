package io.github.rabehx.securify.ui.screens.home.settings.items

import androidx.compose.runtime.Composable
import io.github.rabehx.securify.R
import io.github.rabehx.securify.core.designsystem.component.SwitchPreferenceItem

@Composable
internal fun AmoledThemePreference(
    useAmoledTheme: Boolean,
    enabled: Boolean,
    onAmoledThemeChange: (Boolean) -> Unit,
) = SwitchPreferenceItem(
    title = R.string.amoled_theme,
    subtitle = R.string.amoled_theme_desc,
    checked = useAmoledTheme,
    enabled = enabled,
    onCheckedChange = onAmoledThemeChange,
)
