pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        val properties = java.util.Properties().apply {
            val localProperties = file("local.properties")
            if (localProperties.isFile) {
                localProperties.inputStream().use(::load)
            }
        }
        val gprUser = properties["gpr.user"] as? String ?: System.getenv("GITHUB_ACTOR")
        val gprKey = properties["gpr.key"] as? String ?: System.getenv("GITHUB_TOKEN")
        if (!gprUser.isNullOrBlank() && !gprKey.isNullOrBlank()) {
            maven {
                url = uri("https://maven.pkg.github.com/RabehX/rei")
                credentials {
                    username = gprUser
                    password = gprKey
                }
            }
        }
    }
}

rootProject.name = "Securify"

include(":app")
include(":core:datastore")
include(":core:designsystem")
