package io.github.rabehx.securify.datastore

import androidx.datastore.core.CorruptionException
import io.github.rabehx.securify.datastore.model.Language
import io.github.rabehx.securify.datastore.model.ThemeMode
import io.github.rabehx.securify.datastore.model.UserPreferences
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class UserPreferencesSerializerTest {

    private lateinit var serializer: UserPreferencesSerializer

    @Before
    fun setUp() {
        serializer = UserPreferencesSerializer()
    }

    @Test
    fun defaultValuesAreCorrect() {
        val defaultPreferences = serializer.defaultValue

        assertEquals(Language.SYSTEM, defaultPreferences.language)
        assertEquals(ThemeMode.SYSTEM, defaultPreferences.themeMode)
        assertEquals(UserPreferences.DEFAULT_THEME_SEED_COLOR, defaultPreferences.themeSeedColor)
        assertFalse(defaultPreferences.amoledTheme)
        assertTrue(defaultPreferences.liquidGlass)
        assertFalse(defaultPreferences.dynamicColor)
        assertFalse(defaultPreferences.crashlytics)
        assertFalse(defaultPreferences.analytics)
        assertFalse(defaultPreferences.isBatteryOptimizationNoticeShown)
        assertEquals("", defaultPreferences.logcatExportDirectory)
    }

    @Test
    fun roundTripSerializationSucceeds() = runTest {
        val original = UserPreferences(
            isBatteryOptimizationNoticeShown = true,
            language = Language.ARABIC,
            logcatExportDirectory = "/sdcard/Documents",
            dynamicColor = true,
            themeMode = ThemeMode.DARK,
            themeSeedColor = 0xFF123456L,
            crashlytics = true,
            analytics = true,
            amoledTheme = true,
            liquidGlass = false,
        )

        val output = ByteArrayOutputStream()
        serializer.writeTo(original, output)

        val input = ByteArrayInputStream(output.toByteArray())
        val deserialized = serializer.readFrom(input)

        assertEquals(original, deserialized)
    }

    @Test(expected = CorruptionException::class)
    fun invalidBytesThrowsCorruptionException() = runTest {
        // Corrupted protobuf bytes
        val invalidBytes = byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        val input = ByteArrayInputStream(invalidBytes)

        serializer.readFrom(input)
    }
}
