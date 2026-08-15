plugins {
  id("project-conventions")
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.spring.boot)
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":namecheap-client"))
  implementation(libs.guava)
  implementation(libs.jackson.kotlin)
  implementation(libs.kotlin.reflect)
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.coroutines.reactor)
  implementation(libs.slf4j.api)
  implementation(libs.spring.boot.starter.webclient)
  runtimeOnly(libs.logback)

  testImplementation(project(":test-utils"))
  testImplementation(libs.assertj)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockk)
  testImplementation(libs.mockwebserver)
  testRuntimeOnly(libs.junit.launcher)
}
