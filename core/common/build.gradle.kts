plugins {
    alias(libs.plugins.self.library)
    alias(libs.plugins.self.hilt)
}

android {
    namespace = "io.github.rabehx.securify.core.common"
}

dependencies {
    implementation(libs.bundles.hilt)
    implementation(libs.androidx.core.ktx)
}
