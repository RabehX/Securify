package io.github.rabehx.securify.ui.providable

import androidx.compose.runtime.compositionLocalOf
import io.github.rabehx.securify.datastore.model.UserPreferences

val LocalUserPreferences = compositionLocalOf { UserPreferences() }
