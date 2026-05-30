@file:OptIn(ExperimentalSerializationApi::class)

package io.github.rabehx.securify.datastore.model

import android.os.Environment
import io.github.rabehx.securify.datastore.model.Language
import io.github.rabehx.securify.datastore.model.ThemeMode
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoNumber
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


@Serializable
data class UserPreferences(
    @ProtoNumber(1) val isBatteryOptimizationNoticeShown: Boolean = false,
    @ProtoNumber(2) val language: Language = Language.SYSTEM,
    @ProtoNumber(3) val logcatExportDirectory: String = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOWNLOADS
    ).path,
    @ProtoNumber(4) val dynamicColor: Boolean = false,
    @ProtoNumber(5) val themeMode: ThemeMode = ThemeMode.SYSTEM,
    @ProtoNumber(6) val themeSeedColor: Long = DEFAULT_THEME_SEED_COLOR,
    @ProtoNumber(9) val amoledTheme: Boolean = false,
) {

    @Throws(IOException::class)
    fun encodeTo(output: OutputStream) {
        output.write(ProtoBuf.encodeToByteArray(this))
    }

    companion object {
        const val DEFAULT_THEME_SEED_COLOR: Long = 0xFFA47864L

        @Throws(IOException::class, SerializationException::class)
        fun decodeFrom(input: InputStream): UserPreferences {
            return ProtoBuf.decodeFromByteArray(input.readBytes())
        }
    }
}
