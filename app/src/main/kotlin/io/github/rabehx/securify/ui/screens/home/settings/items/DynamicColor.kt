package io.github.rabehx.securify.ui.screens.home.settings.items

import androidx.compose.runtime.Composable
import com.google.android.material.color.DynamicColors
import io.github.rabehx.securify.R
import io.github.rabehx.securify.core.designsystem.component.SwitchPreferenceItem

@Composable
internal fun DynamicColorPreference(
    useDynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    if (DynamicColors.isDynamicColorAvailable()) {
        SwitchPreferenceItem(
            title = R.string.dynamic_color,
            subtitle = R.string.dynamic_color_desc,
            checked = useDynamicColor,
            enabled = enabled,
            onCheckedChange = onDynamicColorChange,
        )
    }
}
