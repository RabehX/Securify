package io.github.rabehx.securify.ui.screens.home.settings

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.material.color.DynamicColors
import io.github.rabehx.securify.Const
import io.github.rabehx.securify.utils.NetworkResult
import io.github.rabehx.securify.R
import io.github.rabehx.securify.ui.component.AppScaffold
import io.github.rabehx.securify.ui.component.BackButton
import io.github.rabehx.securify.ui.component.PreferenceItem
import io.github.rabehx.securify.ui.component.PreferencesGroup
import io.github.rabehx.securify.ui.component.SectionTitle
import io.github.rabehx.securify.ui.component.ToolbarTitle
import io.github.rabehx.securify.ui.component.TopAppBar
import io.github.rabehx.securify.ui.providable.LocalUserPreferences
import io.github.rabehx.securify.ui.ext.isDarkTheme
import io.github.rabehx.securify.ui.screens.home.settings.items.AmoledThemePreference
import io.github.rabehx.securify.ui.screens.home.settings.items.DynamicColorPreference
import io.github.rabehx.securify.ui.screens.home.settings.items.LanguagePreference
import io.github.rabehx.securify.ui.screens.home.settings.items.LogcatExportPreference
import io.github.rabehx.securify.ui.screens.home.settings.items.PrivacyPolicyPreference
import io.github.rabehx.securify.ui.screens.home.settings.items.TermsServicePreference
import io.github.rabehx.securify.ui.screens.home.settings.items.ThemeColorPreference
import io.github.rabehx.securify.ui.screens.home.settings.items.ThemePreference
import io.github.rabehx.securify.viewmodel.SettingsViewModel

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun Settings(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState(),
            canScroll = { true }
        )

    val context = LocalContext.current
    val userPreferences = LocalUserPreferences.current

    val privacyPolicyState by viewModel.privacyPolicy.collectAsState()
    val termsOfServiceState by viewModel.termsOfService.collectAsState()
    val privacyPolicy = (privacyPolicyState as? NetworkResult.Success)?.data ?: ""
    val termsOfService = (termsOfServiceState as? NetworkResult.Success)?.data ?: ""
    val dynamicColorAvailable = DynamicColors.isDynamicColorAvailable()
    val effectiveDynamicColor = userPreferences.dynamicColor && dynamicColorAvailable


    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    ToolbarTitle(R.string.settings)
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    BackButton(
                        onBack = { onBack.invoke() },
                    )
                },
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            item(key = "general_title") { SectionTitle(R.string.settings_general) }
            item(key = "general_group") {
                PreferencesGroup {
                    LanguagePreference(
                        currentLanguage = userPreferences.language,
                        onLanguageChange = viewModel::setLanguage,
                    )
                    LogcatExportPreference()
                }
            }

            item(key = "appearance_title") { SectionTitle(R.string.settings_appearance) }
            item(key = "appearance_group") {
                PreferencesGroup {
                    DynamicColorPreference(
                        useDynamicColor = effectiveDynamicColor,
                        onDynamicColorChange = viewModel::setDynamicColor,
                    )
                    ThemeColorPreference(
                        currentThemeSeedColor = userPreferences.themeSeedColor,
                        onThemeSeedColorChange = viewModel::setThemeSeedColor,
                        enabled = !effectiveDynamicColor,
                    )
                    ThemePreference(
                        currentTheme = userPreferences.themeMode,
                        onThemeModeChange = viewModel::setThemeMode,
                    )
                    AmoledThemePreference(
                        useAmoledTheme = userPreferences.amoledTheme,
                        enabled = userPreferences.isDarkTheme(),
                        onAmoledThemeChange = viewModel::setAmoledTheme,
                    )
                }
            }

            item(key = "legal_title") { SectionTitle(R.string.settings_legalities) }
            item(key = "legal_group") {
                PreferencesGroup {
                    PrivacyPolicyPreference(
                        content = privacyPolicy,
                        onOpen = { viewModel.fetchPrivacyPolicy(Const.PRIVACY_POLICY_URL) }
                    )
                    TermsServicePreference(
                        content = termsOfService,
                        onOpen = { viewModel.fetchTermsOfService(Const.TERMS_OF_SERVICE_URL) }
                    )
                }
            }

            item(key = "about_title") { SectionTitle(R.string.settings_about) }
            item(key = "about_group") {
                PreferencesGroup {
                    PreferenceItem(
                        title = R.string.support,
                        subtitle = R.string.developer_telegram_url,
                        onClick = { context.openUrl(Const.DEVELOPER_TELEGRAM_URL) },
                    )
                    PreferenceItem(
                        title = R.string.github,
                        subtitle = R.string.developer_github_url,
                        onClick = { context.openUrl(Const.DEVELOPER_GITHUB_URL) },
                    )
                    PreferenceItem(
                        title = R.string.telegram,
                        subtitle = R.string.developer_telegram_url,
                        onClick = { context.openUrl(Const.DEVELOPER_TELEGRAM_URL) },
                    )
                }
            }

            item(key = "bottom_spacer") { Spacer(Modifier.padding(top = 16.dp)) }
        }
    }
}

private fun Context.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    runCatching { startActivity(intent) }
}
