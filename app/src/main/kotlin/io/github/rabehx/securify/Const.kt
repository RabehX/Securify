package io.github.rabehx.securify

import android.os.Build

object Const {
    const val VERSION_NAME = BuildConfig.VERSION_NAME
    const val VERSION_CODE = BuildConfig.VERSION_CODE
    val KERNEL_VERSION: String by lazy {
        runCatching {
            Runtime.getRuntime().exec("uname -r").inputStream.bufferedReader().readLine()
        }.getOrElse { "Unknown" }
    }

    val DEVICE_NAME = "${Build.MANUFACTURER} ${Build.MODEL}"
    val SYSTEM_ABI = Build.SUPPORTED_ABIS.firstOrNull() ?: "Unknown"
    val SYSTEM_VERSION : String = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    val SECURITY_PATCH_LEVEL: String = Build.VERSION.SECURITY_PATCH
    val FINGERPRINT: String = Build.FINGERPRINT

    const val DEVELOPER_GITHUB_URL = "https://github.com/RabehX"
    const val DEVELOPER_TELEGRAM_URL = "https://t.me/RabehX"

    private const val BASE_RAW_URL =
        "https://raw.githubusercontent.com/RabehX/Securify/master/docs/"

    const val PRIVACY_POLICY_URL = "${BASE_RAW_URL}PRIVACY_POLICY.md"
    const val TERMS_OF_SERVICE_URL = "${BASE_RAW_URL}TERMS_OF_SERVICE.md"
}
