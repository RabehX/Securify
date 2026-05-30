plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.lsp) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.projectguard) apply true
}

projectGuard {
    report {
        showLibrariesInGraph = true
    }

    // ---------- Layer separation rules ----------

    // Data modules must not pull in UI
    guard(":core:datastore") {
        deny(":core:designsystem")
    }

    // UI / design-system modules must not pull in data
    guard(":core:designsystem") {
        deny(":core:datastore")
    }

    // ---------- Library restriction rules ----------

    // mockk is a test-only tool — prefer fakes in non-app modules
    restrictDependency(libs.mockk) {
        reason("Use fakes/stubs instead of mocks. mockk is only allowed in :app tests.")
        allow(":app")
    }

    // Network stack should stay out of pure-data / UI core modules
    restrictDependency(libs.retrofit.core) {
        reason("Retrofit should only be used in :app for now.")
        allow(":app")
    }

    // ---------- App module ----------
    restrictModule(":app") {
        // :app orchestrates everything — all external libraries are fine here
        allowExternalLibraries()
    }
}

tasks.register<Delete>("clean", fun Delete.() {
    delete(layout.buildDirectory)
})
