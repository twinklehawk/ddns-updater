rootProject.name = "ddns-updater"

dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

include(
  "app",
  "namecheap-client",
  "test-utils",
)
