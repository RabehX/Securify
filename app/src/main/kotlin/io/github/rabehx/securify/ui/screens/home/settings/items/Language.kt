@file:Suppress("DEPRECATION")

package io.github.rabehx.securify.ui.screens.home.settings.items

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import io.github.rabehx.securify.R
import io.github.rabehx.securify.datastore.model.Language
import io.github.rabehx.securify.core.designsystem.component.BottomSheet
import io.github.rabehx.securify.core.designsystem.component.PreferenceItem
import io.github.rabehx.securify.core.designsystem.component.PreferencesGroup
import io.github.rabehx.securify.core.designsystem.component.SelectorItem
import io.github.rabehx.securify.core.designsystem.component.SheetTitle
import java.util.Locale

private enum class LanguageItem(
    val value: Language,
    val text: Int,
    val locale: Locale?
) {
    SYSTEM(Language.SYSTEM, R.string.system_default, null),
    ENGLISH(Language.ENGLISH, R.string.en, Locale.forLanguageTag("en")),
    ARABIC(Language.ARABIC, R.string.ar, Locale.forLanguageTag("ar"))
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun LanguagePreference(
    currentLanguage: Language,
    onLanguageChange: (Language) -> Unit,
) {
    var openBottomSheet by remember { mutableStateOf(false) }

    val currentLanguageItem = remember(currentLanguage) {
        LanguageItem.entries.first { it.value == currentLanguage }
    }

    if (openBottomSheet) {
        BottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = rememberModalBottomSheetState(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SheetTitle(stringResource(R.string.language))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LanguageItem.entries.forEach { languageOption ->
                        val onSelect = {
                            onLanguageChange(languageOption.value)
                            val localeList = languageOption.locale
                                ?.let { LocaleListCompat.create(it) }
                                ?: LocaleListCompat.getEmptyLocaleList()
                            AppCompatDelegate.setApplicationLocales(localeList)
                        }

                        SelectorItem(
                            key = languageOption.name,
                            selectedKey = currentLanguageItem.name,
                            label = stringResource(languageOption.text),
                            onSelectionChanged = { onSelect() },
                        )
                    }
                }
            }
        }
    }

    PreferenceItem(
        title = R.string.language,
        subtitle = currentLanguageItem.text,
        onClick = { openBottomSheet = true },
    )
}