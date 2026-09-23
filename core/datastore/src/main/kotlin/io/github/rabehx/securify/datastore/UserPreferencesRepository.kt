package io.github.rabehx.securify.datastore

import io.github.rabehx.securify.datastore.model.Language
import io.github.rabehx.securify.datastore.model.ThemeMode
import io.github.rabehx.securify.datastore.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val preferencesFlow: Flow<UserPreferences>

    suspend fun setLanguage(language: Language)
    suspend fun setDynamicColor(dynamicColor: Boolean)
    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun setThemeSeedColor(seedColor: Long)
    suspend fun setAmoledTheme(enabled: Boolean)
    suspend fun setLiquidGlass(enabled: Boolean)
    suspend fun setCrashlytics(crashlytics: Boolean)
    suspend fun setAnalytics(analytics: Boolean)
}
