package io.github.rabehx.securify.ui.screens.home.settings.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import io.github.rabehx.securify.datastore.model.ThemeMode
import io.github.rabehx.securify.core.designsystem.component.BottomSheet
import io.github.rabehx.securify.core.designsystem.component.PreferenceItem
import io.github.rabehx.securify.core.designsystem.component.SelectorItem
import io.github.rabehx.securify.core.designsystem.component.SheetTitle

private enum class ThemeModeItem(
    val value: ThemeMode,
    val text: Int
) {
    SYSTEM(value = ThemeMode.SYSTEM, text = R.string.system_default),
    LIGHT(value = ThemeMode.LIGHT, text = R.string.light),
    DARK(value = ThemeMode.DARK, text = R.string.dark),
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ThemePreference(
    currentTheme: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val currentThemeItem = remember(currentTheme) {
        ThemeModeItem.entries.first { it.value == currentTheme }
    }

    if (openBottomSheet) {
        BottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = rememberBottomSheetState(
                initialValue = SheetValue.Expanded,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SheetTitle("Select Theme")

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ThemeModeItem.entries.forEach { mode ->
                        val onSelect = {
                            onThemeModeChange(mode.value)
                        }

                        SelectorItem(
                            key = mode.value.name,
                            selectedKey = currentTheme.name,
                            label = stringResource(mode.text),
                            onSelectionChanged = { onSelect() },
                        )
                    }
                }
            }
        }
    }

    PreferenceItem(
        title = R.string.theme,
        subtitle = currentThemeItem.text,
        onClick = { openBottomSheet = true },
    )
}
