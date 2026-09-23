plugins {
    alias(libs.plugins.self.library)
    alias(libs.plugins.self.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.rabehx.securify.core.network"
}

dependencies {
    api(libs.bundles.network)
    api(libs.bundles.serialization)
    api(libs.okhttp)

    testImplementation(libs.junit)
}

