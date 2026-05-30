import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureCompose() {
    extensions.configure<KotlinAndroidProjectExtension> {
        sourceSets.all {
            languageSettings {
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
                optIn("androidx.compose.foundation.ExperimentalFoundationApi")
                optIn("androidx.compose.foundation.layout.ExperimentalLayoutApi")
            }
        }
    }

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        "implementation"(libs.findLibrary("androidx.compose.material3").get())
        "implementation"(libs.findLibrary("androidx.compose.ui").get())
        "implementation"(libs.findLibrary("androidx.compose.ui.tooling.preview").get())
        "debugImplementation"(libs.findLibrary("androidx.compose.ui.tooling").get())
    }
}
