plugins {
  id("project-conventions")
  `java-library`
}

repositories {
  mavenCentral()
}

dependencies {
  api(libs.spring.webflux)
  implementation(libs.kotlin.reflect)
  implementation(libs.kotlin.stdlib)
  implementation(libs.slf4j.api)
  implementation(libs.kotlinx.coroutines.reactor)

  testImplementation(project(":test-utils"))
  testImplementation(libs.junit)
  testImplementation(libs.mockk)
  testImplementation(libs.assertj)
  testImplementation(libs.mockwebserver)
  testImplementation(libs.kotlinx.coroutines.test)
  testRuntimeOnly(libs.junit.launcher)
  testRuntimeOnly(libs.logback)
}
