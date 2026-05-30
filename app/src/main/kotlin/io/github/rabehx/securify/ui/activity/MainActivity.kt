package io.github.rabehx.securify.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.AndroidEntryPoint
import io.github.rabehx.securify.MainNavigation
import io.github.rabehx.securify.datastore.UserPreferencesRepository
import io.github.rabehx.securify.ui.providable.LocalUserPreferences
import io.github.rabehx.securify.core.designsystem.theme.AppTheme
import io.github.rabehx.securify.ui.ext.isDarkTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val userPreferences by userPreferencesRepository.preferencesFlow
                .collectAsStateWithLifecycle(initialValue = null)

            val preferences = if (userPreferences == null) {
                return@setContent
            } else {
                checkNotNull(userPreferences)
            }

            val dynamicColorAvailable = DynamicColors.isDynamicColorAvailable()
            val useDynamicColor = preferences.dynamicColor && dynamicColorAvailable
            val useAmoledTheme =
                preferences.amoledTheme && preferences.isDarkTheme()

            CompositionLocalProvider(
                LocalUserPreferences provides preferences
            ) {
                AppTheme(
                    useDynamicColor = useDynamicColor,
                    useDarkTheme = preferences.isDarkTheme(),
                    seedColor = preferences.themeSeedColor,
                    useAmoledTheme = useAmoledTheme,
                ) {
                    MainNavigation()
                }
            }
        }
    }
}
