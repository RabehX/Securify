@file:Suppress("unused")

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            apply(plugin = "com.android.library")

            extensions.configure<LibraryExtension> {
                compileSdk = 37
                buildToolsVersion = "37.0.0"

                defaultConfig {
                    minSdk = 26
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
            }
        }
}
