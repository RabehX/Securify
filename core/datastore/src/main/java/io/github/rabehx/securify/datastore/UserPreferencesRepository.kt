package io.github.rabehx.securify.datastore

import io.github.rabehx.securify.datastore.model.Language
import io.github.rabehx.securify.datastore.model.ThemeMode
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataStore
) {
    val preferencesFlow = userPreferencesDataSource.preferencesFlow

    suspend fun setLanguage(language: Language) =
        userPreferencesDataSource.setLanguage(language)
    suspend fun setDynamicColor(dynamicColor: Boolean) =
        userPreferencesDataSource.setDynamicColor(dynamicColor)

    suspend fun setThemeMode(themeMode: ThemeMode) =
        userPreferencesDataSource.setThemeMode(themeMode)

    suspend fun setThemeSeedColor(seedColor: Long) =
        userPreferencesDataSource.setThemeSeedColor(seedColor)

    suspend fun setAmoledTheme(enabled: Boolean) =
        userPreferencesDataSource.setAmoledTheme(enabled)
}
