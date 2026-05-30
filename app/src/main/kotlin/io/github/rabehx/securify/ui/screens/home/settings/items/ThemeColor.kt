package io.github.rabehx.securify.ui.screens.home.settings.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rabehx.securify.R
import io.github.rabehx.securify.datastore.model.UserPreferences
import io.github.rabehx.securify.core.designsystem.component.BottomSheet
import io.github.rabehx.securify.core.designsystem.component.PreferenceItem
import io.github.rabehx.securify.core.designsystem.component.SelectorItem
import io.github.rabehx.securify.core.designsystem.component.SheetTitle

private data class ThemeColorItem(
    val seedColor: Long,
    val text: Int,
)

private val themeColorItems = listOf(
    ThemeColorItem(UserPreferences.DEFAULT_THEME_SEED_COLOR, R.string.theme_color_mocha_mousse),
    ThemeColorItem(0xFFFFBE98L, R.string.theme_color_peach_fuzz),
    ThemeColorItem(0xFFBB2649L, R.string.theme_color_viva_magenta),
    ThemeColorItem(0xFF6667ABL, R.string.theme_color_very_peri),
    ThemeColorItem(0xFF939597L, R.string.theme_color_ultimate_gray),
    ThemeColorItem(0xFFF5DF4DL, R.string.theme_color_illuminating),
    ThemeColorItem(0xFF0F4C81L, R.string.theme_color_classic_blue),
    ThemeColorItem(0xFFFF6F61L, R.string.theme_color_living_coral),
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ThemeColorPreference(
    currentThemeSeedColor: Long,
    onThemeSeedColorChange: (Long) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val currentThemeColor = remember(currentThemeSeedColor) {
        themeColorItems.firstOrNull { it.seedColor == currentThemeSeedColor }
            ?: themeColorItems.first()
    }

    if (openBottomSheet) {
        BottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = rememberBottomSheetState(
                initialValue = SheetValue.Expanded,
                confirmValueChange = { true }
            ),
        ) {
            Box(modifier = modifier.fillMaxSize()) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SheetTitle(stringResource(R.string.select_theme_color))

                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        themeColorItems.forEach { colorOption ->
                            SelectorItem(
                                key = colorOption.seedColor.toString(),
                                selectedKey = currentThemeColor.seedColor.toString(),
                                label = stringResource(colorOption.text),
                                onSelectionChanged = { onThemeSeedColorChange(colorOption.seedColor) },
                            )
                        }
                    }
                }
            }
        }
    }

    PreferenceItem(
        title = R.string.theme_color,
        subtitle = currentThemeColor.text,
        enabled = enabled,
        onClick = { openBottomSheet = true },
    )
}
