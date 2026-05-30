package io.github.rabehx.securify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rabehx.securify.utils.NetworkResult
import io.github.rabehx.securify.datastore.model.Language
import io.github.rabehx.securify.datastore.model.ThemeMode
import io.github.rabehx.securify.datastore.UserPreferencesRepository
import io.github.rabehx.securify.datastore.model.UserPreferences
import io.github.rabehx.securify.repository.LegalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val legalRepository: LegalRepository,
) : ViewModel() {

    val userPreferences = userPreferencesRepository.preferencesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserPreferences()
    )

    private val privacyPolicyFlow = MutableStateFlow<NetworkResult<String>?>(null)
    val privacyPolicy: StateFlow<NetworkResult<String>?> = privacyPolicyFlow.asStateFlow()

    private val termsOfServiceFlow = MutableStateFlow<NetworkResult<String>?>(null)
    val termsOfService: StateFlow<NetworkResult<String>?> = termsOfServiceFlow.asStateFlow()

    private fun updatePreference(update: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                update()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to update preference", e)
            }
        }
    }

    fun fetchPrivacyPolicy(url: String) {
        viewModelScope.launch {
            legalRepository.fetchMarkdown(url).collect { result ->
                privacyPolicyFlow.value = result
            }
        }
    }

    fun fetchTermsOfService(url: String) {
        viewModelScope.launch {
            legalRepository.fetchMarkdown(url).collect { result ->
                termsOfServiceFlow.value = result
            }
        }
    }

    fun setLanguage(language: Language) =
        updatePreference { userPreferencesRepository.setLanguage(language) }

    fun setThemeMode(theme: ThemeMode) =
        updatePreference { userPreferencesRepository.setThemeMode(theme) }

    fun setThemeSeedColor(seedColor: Long) {
        updatePreference {
            userPreferencesRepository.setThemeSeedColor(seedColor)
            userPreferencesRepository.setDynamicColor(false)
        }
    }

    fun setDynamicColor(enabled: Boolean) {
        updatePreference { userPreferencesRepository.setDynamicColor(enabled) }
    }

    fun setAmoledTheme(enabled: Boolean) {
        updatePreference { userPreferencesRepository.setAmoledTheme(enabled) }
    }

    companion object {
        private const val TAG = "SettingsViewModel"
    }
}
