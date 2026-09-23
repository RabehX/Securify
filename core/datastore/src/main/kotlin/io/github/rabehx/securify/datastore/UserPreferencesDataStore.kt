package io.github.rabehx.securify.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import io.github.rabehx.securify.core.common.di.IoDispatcher
import io.github.rabehx.securify.datastore.model.Language
import io.github.rabehx.securify.datastore.model.ThemeMode
import io.github.rabehx.securify.datastore.model.UserPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesDataStore @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : UserPreferencesRepository {

    override val preferencesFlow: Flow<UserPreferences> = userPreferences.data
        .catch { exception ->
            Log.e(TAG, "Error reading user preferences", exception)
            emit(UserPreferences())
        }
        .flowOn(ioDispatcher)

    private suspend fun updatePreference(transform: (UserPreferences) -> UserPreferences) {
        withContext(ioDispatcher) {
            try {
                userPreferences.updateData(transform)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to update user preferences", e)
            }
        }
    }

    override suspend fun setLanguage(language: Language) {
        updatePreference { it.copy(language = language) }
    }

    override suspend fun setCrashlytics(crashlytics: Boolean) {
        updatePreference { it.copy(crashlytics = crashlytics) }
    }

    override suspend fun setAnalytics(analytics: Boolean) {
        updatePreference { it.copy(analytics = analytics) }
    }

    override suspend fun setDynamicColor(dynamicColor: Boolean) {
        updatePreference { it.copy(dynamicColor = dynamicColor) }
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        updatePreference { it.copy(themeMode = themeMode) }
    }

    override suspend fun setThemeSeedColor(seedColor: Long) {
        updatePreference { it.copy(themeSeedColor = seedColor) }
    }

    override suspend fun setAmoledTheme(enabled: Boolean) {
        updatePreference { it.copy(amoledTheme = enabled) }
    }

    override suspend fun setLiquidGlass(enabled: Boolean) {
        updatePreference { it.copy(liquidGlass = enabled) }
    }

    companion object {
        private const val TAG = "UserPreferencesDataStore"
    }
}
