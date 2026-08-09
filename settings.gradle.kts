rootProject.name = "ddns-updater"

dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

include(
  "app",
  "test-utils",
)
