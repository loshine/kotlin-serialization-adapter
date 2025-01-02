plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
    `maven-publish`
}

group = "io.github.loshine.json.gradle"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    implementation(libs.kotlin.gradle.plugin.api)
}

gradlePlugin {
    plugins {
        create("jsonAdapterPlugin") {
            // users will do `apply plugin:"io.github.loshine.json.adapter"`
            id = "io.github.loshine.json.adapter"
            // entry-point class
            implementationClass = "io.github.loshine.json.gradle.JsonCompilerGradleSubPlugin"
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            val component = components.find {
                it.name == "java" || it.name == "release"
            }
            from(component)
        }
    }
}

tasks.register("sourcesJar", Jar::class) {
    group = "build"
    description = "Assembles Kotlin sources"

    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
    dependsOn(tasks.classes)
}
