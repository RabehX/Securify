@file:Suppress("unused")

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            apply(plugin = "com.android.application")

            extensions.configure<ApplicationExtension> {
                compileSdk = 37
                buildToolsVersion = "37.0.0"

                defaultConfig {
                    minSdk = 26
                    targetSdk = 37
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_25
                    targetCompatibility = JavaVersion.VERSION_25
                }
            }

            extensions.configure<JavaPluginExtension> {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(25))
                }
            }

            extensions.configure<KotlinAndroidProjectExtension> {
                jvmToolchain(25)

                sourceSets.all {
                    languageSettings {
                        optIn("kotlin.ExperimentalStdlibApi")
                        optIn("kotlinx.coroutines.FlowPreview")
                    }
                }
            }
        }
}
