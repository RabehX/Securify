package io.github.rabehx.securify.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import io.github.rabehx.securify.datastore.model.Language
import io.github.rabehx.securify.datastore.model.ThemeMode
import io.github.rabehx.securify.datastore.model.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserPreferencesDataStore @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val preferencesFlow: Flow<UserPreferences> = userPreferences.data
        .catch { exception ->
            Log.e(TAG, "Error reading user preferences", exception)
            emit(UserPreferences())
        }
        .flowOn(Dispatchers.IO)

    private suspend fun updatePreference(transform: (UserPreferences) -> UserPreferences) {
        withContext(Dispatchers.IO) {
            try {
                userPreferences.updateData(transform)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to update user preferences", e)
            }
        }
    }

    suspend fun setLanguage(language: Language) =
        updatePreference { it.copy(language = language) }

    suspend fun setDynamicColor(enabled: Boolean) =
        updatePreference { it.copy(dynamicColor = enabled) }

    suspend fun setThemeMode(mode: ThemeMode) =
        updatePreference { it.copy(themeMode = mode) }

    suspend fun setThemeSeedColor(seedColor: Long) =
        updatePreference { it.copy(themeSeedColor = seedColor) }

    suspend fun setAmoledTheme(enabled: Boolean) =
        updatePreference { it.copy(amoledTheme = enabled) }

    companion object {
        private const val TAG = "UserPreferencesRepo"
    }
}
