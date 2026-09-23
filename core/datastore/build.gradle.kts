plugins {
    alias(libs.plugins.self.library)
    alias(libs.plugins.self.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.rabehx.securify.core.datastore"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.androidx.datastore.core)
    implementation(libs.bundles.serialization)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}
