plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.spring") version "2.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.springframework.boot") version "3.4.1"
    jacoco
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    id("org.jmailen.kotlinter") version "5.0.1"
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.1")
        mavenBom("org.jetbrains.kotlin:kotlin-bom:2.1.0")
        mavenBom("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.9.0")
    }
    dependencies {
        dependency("io.mockk:mockk:1.13.13")
        dependency("com.google.guava:guava:33.4.0-jre")
        dependency("com.squareup.okhttp3:mockwebserver:4.12.0")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.guava:guava")
    implementation("org.slf4j:slf4j-api")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    runtimeOnly("ch.qos.logback:logback-classic")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.mockk:mockk")
    testImplementation("org.assertj:assertj-core")
    testImplementation("com.squareup.okhttp3:mockwebserver")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

configurations.matching { it.name == "detekt" }.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion("2.0.10")
        }
    }
}
