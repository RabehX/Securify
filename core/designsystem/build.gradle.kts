plugins {
    alias(libs.plugins.self.library)
    alias(libs.plugins.self.compose.library)
}

android {
    namespace = "io.github.rabehx.securify.core.designsystem"
}

dependencies {
    implementation(libs.bundles.compose)
    implementation(libs.bundles.material3)
    implementation(libs.tabler.icon)
}
