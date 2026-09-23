plugins {
    alias(libs.plugins.self.library)
}

android {
    namespace = "io.github.rabehx.securify.core.common"
}

dependencies {
    implementation(libs.bundles.hilt)
}
