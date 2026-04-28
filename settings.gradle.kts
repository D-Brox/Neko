pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

dependencyResolutionManagement {
  versionCatalogs {
    create("kotlinx") { from(files("gradle/kotlinx.versions.toml")) }
    create("androidx") { from(files("gradle/androidx.versions.toml")) }
    create("compose") { from(files("gradle/compose.versions.toml")) }
  }
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    google()
    maven { setUrl("https://jitpack.io") }
  }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Neko"

include(":app")

include(":core")

include(":constants")
