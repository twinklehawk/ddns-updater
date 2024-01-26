import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.springframework.boot") version "3.2.2"
    jacoco
    id("io.gitlab.arturbosch.detekt") version "1.23.4"
    id("org.jmailen.kotlinter") version "4.2.0"
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.2.2")
        mavenBom("org.jetbrains.kotlin:kotlin-bom:1.9.22")
        mavenBom("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.7.3")
    }
    dependencies {
        dependency("io.mockk:mockk:1.13.9")
        dependency("com.google.guava:guava:33.0.0-jre")
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

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
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
            useVersion("1.9.21")
        }
    }
}
