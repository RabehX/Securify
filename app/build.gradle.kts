import org.lsposed.lsparanoid.plugin.LSParanoidExtension
import java.time.Instant
import java.util.Properties

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use(::load)
}

val apiUrl = localProperties["api.url"] as? String
    ?: System.getenv("API_URL")
    ?: ""
val cloudProjectNumber = (localProperties["cloud.projectNumber"] as? String)?.toLongOrNull()
    ?: System.getenv("CLOUD_PROJECT_NUMBER")?.toLongOrNull()
    ?: 0L
val supportedAbis = listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
val releaseKeystoreProperties = listOf("storeFile", "storePassword", "keyAlias", "keyPassword")

plugins {
    alias(libs.plugins.self.application)
    alias(libs.plugins.self.compose)
    alias(libs.plugins.lsp)
    alias(libs.plugins.self.hilt)
    alias(libs.plugins.kotlin.serialization)
}

val keystoreProperties = Properties().apply {
    val keystorePropertiesFile = rootProject.file("keystore.properties")
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use(::load)
    }
}
val hasSigningConfig = releaseKeystoreProperties.all { key ->
    !keystoreProperties.getProperty(key).isNullOrBlank()
}

extensions.configure<LSParanoidExtension>("lsparanoid") {
    variantFilter = { variant -> variant.buildType == "release" }
}

android {
    namespace = "io.github.rabehx.securify"

    buildFeatures {
        buildConfig = true
    }

    signingConfigs {
        if (hasSigningConfig) {
            create("release") {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    defaultConfig {
        applicationId = "io.github.rabehx.securify"
        versionCode = 10400
        versionName = "1.4.0"
        buildConfigField("long", "BUILD_TIME", "${Instant.now().epochSecond}L")
        buildConfigField("String", "API_URL", "\"$apiUrl\"")
        buildConfigField("long", "CLOUD_PROJECT_NUMBER", "${cloudProjectNumber}L")

        ndk {
            abiFilters += supportedAbis
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            if (hasSigningConfig) {
                signingConfig = signingConfigs.getByName("release")
            }

            ndk {
                debugSymbolLevel = "SYMBOL_TABLE"
            }
        }
    }
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.material3)
    implementation(libs.bundles.adaptive)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.lifecycle)
    implementation(libs.androidx.hilt)
    implementation(libs.bundles.serialization)
    implementation(libs.bundles.material)
    implementation(libs.okhttp)
    implementation(libs.rei)
    implementation(libs.tabler.icon)
    implementation(libs.bundles.network)
    implementation(libs.integrity)
    implementation(libs.kotlinx.coroutines.play.services)
    testImplementation(libs.bundles.testing)
}
