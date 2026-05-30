plugins {
    alias(libs.plugins.self.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "io.github.rabehx.securify.core.datastore"
}

dependencies {
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.datastore.core)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.serialization)
}
